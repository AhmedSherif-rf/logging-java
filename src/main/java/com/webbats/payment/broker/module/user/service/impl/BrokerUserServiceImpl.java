package com.webbats.payment.broker.module.user.service.impl;

import com.webbats.payment.broker.common.constants.AppConstants;
import com.webbats.payment.broker.common.entity.Broker;
import com.webbats.payment.broker.common.entity.Country;
import com.webbats.payment.broker.common.entity.Language;
import com.webbats.payment.broker.common.entity.Role;
import com.webbats.payment.broker.common.entity.User;
import com.webbats.payment.broker.common.enums.UserType;
import com.webbats.payment.broker.common.exception.Errors;
import com.webbats.payment.broker.common.exception.NotFoundException;
import com.webbats.payment.broker.common.exception.ValidationFailureException;
import com.webbats.payment.broker.common.repo.BrokerRepository;
import com.webbats.payment.broker.common.repo.CountryRepository;
import com.webbats.payment.broker.common.repo.LanguageRepository;
import com.webbats.payment.broker.common.repo.UserRepository;
import com.webbats.payment.broker.common.response.PageResponse;
import com.webbats.payment.broker.common.service.BrokerEmailNotificationService;
import com.webbats.payment.broker.module.user.enums.UserSortableColumns;
import com.webbats.payment.broker.module.user.repo.RoleRepository;
import com.webbats.payment.broker.module.user.request.BrokerUserCreateRequestDto;
import com.webbats.payment.broker.module.user.request.BrokerUserFilterCriteria;
import com.webbats.payment.broker.module.user.request.BrokerUserUpdateRequest;
import com.webbats.payment.broker.module.user.request.UserRequestDTO;
import com.webbats.payment.broker.module.user.response.BrokerUserPageResponseDto;
import com.webbats.payment.broker.module.user.response.BrokerUserResponseDTO;
import com.webbats.payment.broker.module.user.response.UserResponseDTO;
import com.webbats.payment.broker.module.user.service.BrokerUserService;
import com.webbats.payment.broker.module.user.specification.UserSpecification;
import com.webbats.payment.broker.utility.PasswordGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrokerUserServiceImpl implements BrokerUserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CountryRepository countryRepository;
    private final LanguageRepository languageRepository;
    private final BrokerRepository brokerRepository;
    private final PasswordGeneratorService passwordGeneratorService;
    private final BrokerEmailNotificationService brokerEmailNotificationService;

    @Override
    @Transactional
    public BrokerUserResponseDTO createBrokerUser(BrokerUserCreateRequestDto brokerUserCreateRequestDto) {
        UserRequestDTO request = brokerUserCreateRequestDto.getUser();

        validatePhoneNumber(request.getPhoneNumber());
        validateUniqueEmail(request.getEmail());

        Set<Language> languages = new HashSet<>();
        for (String langCode : request.getLanguages()) {
            Language language = languageRepository.findById(langCode)
                    .orElseThrow(() -> new ValidationFailureException(Errors.LANGUAGE_NOT_EXIST));
            languages.add(language);
        }

        // Validate if roles exist
        Set<Role> roles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            Role role = roleRepository.findById(roleName)
                    .orElseThrow(() -> new ValidationFailureException(Errors.ROLE_NOT_EXIST));
            roles.add(role);
        }

        Country country = countryRepository.findById(request.getCountry())
                .orElseThrow(() -> new ValidationFailureException(Errors.ERROR_COUNTRY_NOT_SUPPORTED));

        String password = passwordGeneratorService.generatePassword();

        User user = new User();
        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setFullName(request.getFirstName() + " " + request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhoneNumber());
        user.setCountry(country);
        user.setLanguages(languages);
        user.setRoles(roles);
        user.setUserType(UserType.BROKER);
        user.setTermsAccepted(request.isTermsAccepted());
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(brokerUserCreateRequestDto.getStatus());
        user.setEnabled(request.isEnabled());
        user.setActive(request.isActive());
        userRepository.save(user);

        Broker broker = Broker.builder()
                .canHandleAppeals(brokerUserCreateRequestDto.isCanHandleAppeals())
                .user(user)
                .build();
        brokerRepository.save(broker);
        brokerEmailNotificationService.sendNewUserEmail(user.getId(), password);
        return convertToDto(user,broker);
    }

    private void validateUniqueEmail(String email) {
        if (!isEmailUnique(email)) {
            throw new ValidationFailureException(Errors.EMAIL_ALREADY_EXIST);
        }
    }

    private BrokerUserResponseDTO convertToDto(User user, Broker broker) {
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstname())
                .lastName(user.getLastname())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhone())
                .enabled(user.isEnabled())
                .languages(user.getLanguages().stream().map(Language::getLangCode).toList())
                .online(user.isOnline())
                .active(user.isActive())
                .termsAccepted(user.isTermsAccepted())
                .country(user.getCountry().getCountryCode())
                .roles(user.getRoles().stream().map(Role::getId).toList())
                .build();

        return BrokerUserResponseDTO.builder()
                .brokerId(broker.getBrokerId())
                .user(userResponseDTO)
                .canHandleAppeals(broker.getCanHandleAppeals())
                .status(user.getStatus())
                .build();
    }

    private BrokerUserPageResponseDto convertToPageResponseDto(User user) {
        Long brokerId = null;

        Optional<Broker> broker = brokerRepository.findByUser(user);
        if(broker.isPresent()){
            brokerId = broker.get().getBrokerId();
        }

        return BrokerUserPageResponseDto.builder()
                .emailAddress(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getId).toList())
                .status(user.getStatus())
                .countryCode(user.getCountry().getCountryCode())
                .fullName(user.getFullName())
                .languages(user.getLanguages().stream().map(Language::getLangCode).toList())
                .brokerId(brokerId)
                .build();
    }

    @Override
    public PageResponse<BrokerUserPageResponseDto> getBrokerUsers(BrokerUserFilterCriteria brokerUserFilterCriteria) {
        Pageable pageable = brokerUserFilterCriteria.getSortablePageRequest(UserSortableColumns::getEntityFieldName);
        Specification<User> filterCriteria = UserSpecification.getUsersByFilterCriteria(brokerUserFilterCriteria);
        Page<User> usersList = userRepository.findAll(filterCriteria, pageable);
        long total = userRepository.count(filterCriteria);
        List<BrokerUserPageResponseDto> pageResponseDtoList = usersList.stream()
                .map(this::convertToPageResponseDto)
                .toList();
        return PageResponse.<BrokerUserPageResponseDto>builder()
                .content(pageResponseDtoList)
                .pageNumber(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalElements(total)
                .build();
    }

    @Override
    public BrokerUserResponseDTO getBrokerUserById(Long userId) {
        User user = userRepository.findByIdAndUserType(userId, UserType.BROKER)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_NO_RECORD.message()));

        Broker broker = brokerRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_NO_RECORD.message()));
        return convertToDto(user, broker);
    }

    @Override
    @Transactional
    public BrokerUserResponseDTO updateBrokerUser(BrokerUserUpdateRequest brokerUserUpdateRequest, Long userId, User loggedInUser) {
        String password = null;
        User user = userRepository.findByIdAndUserType(userId, UserType.BROKER)
                .orElseThrow(() -> new NotFoundException("Requested user not found"));
        Broker broker = brokerRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_NO_RECORD.message()));

        UserRequestDTO request = brokerUserUpdateRequest.getUser();

        validateLoggedInUser(loggedInUser, user);

        validatePhoneNumber(request.getPhoneNumber());

        if (!user.getEmail().equals(request.getEmail())) {
            validateUniqueEmail(request.getEmail());
            password = passwordGeneratorService.generatePassword();
        }

        Set<Language> languages = new HashSet<>();
        for (String langCode : request.getLanguages()) {
            Language language = languageRepository.findById(langCode)
                    .orElseThrow(() -> new ValidationFailureException(Errors.LANGUAGE_NOT_EXIST));
            languages.add(language);
        }

        // Validate if roles exist
        Set<Role> roles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            Role role = roleRepository.findById(roleName)
                    .orElseThrow(() -> new ValidationFailureException(Errors.ROLE_NOT_EXIST));
            roles.add(role);
        }

        Country country = countryRepository.findById(request.getCountry())
                .orElseThrow(() -> new ValidationFailureException(Errors.ERROR_COUNTRY_NOT_SUPPORTED));

        user.setFirstname(request.getFirstName());
        user.setLastname(request.getLastName());
        user.setFullName(request.getFirstName() + " " + request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhoneNumber());
        user.setCountry(country);
        user.setLanguages(languages);
        user.setRoles(roles);
        user.setTermsAccepted(request.isTermsAccepted());
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setStatus(brokerUserUpdateRequest.getStatus());
        userRepository.save(user);
        broker.setCanHandleAppeals(brokerUserUpdateRequest.isCanHandleAppeals());
        brokerRepository.save(broker);
        if (!StringUtils.isEmpty(password)) {
            brokerEmailNotificationService.sendUserPasswordChangeEmail(user.getId(), password);
        }
        return convertToDto(user, broker);
    }

    private void validateLoggedInUser(User loggedInUser, User user) {
        if (user.getId().equals(loggedInUser.getId())) {
            throw new ValidationFailureException(Errors.CANNOT_UPDATE_OWN_DETAILS);
        }
    }


    private void validatePasswordConstraint(String password) {
        if (!password.matches(AppConstants.PASSWORD_REGEX))
            throw new ValidationFailureException(Errors.PASSWORD_VALIDATION);
    }

    private boolean isEmailUnique(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        return userByEmail.isEmpty();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void validatePhoneNumber(String phone) {
        if (!phone.matches(AppConstants.PHONE_REGEX) || !(phone.length() >= 8 && phone.length() <= 16))
            throw new ValidationFailureException(Errors.PHONE_VALIDATION);
    }

    @Override
    @Transactional
    public void changeBrokerUserPassword(Long userId, User loggedInUser) {
        User retreivedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Errors.ERROR_NO_RECORD.message()));
        validateLoggedInUser(loggedInUser, retreivedUser);
        String password = passwordGeneratorService.generatePassword();
        retreivedUser.setPassword(encodePassword(password));
        userRepository.save(retreivedUser);
        brokerEmailNotificationService.sendUserPasswordChangeEmail(retreivedUser.getId(), password);
    }
}