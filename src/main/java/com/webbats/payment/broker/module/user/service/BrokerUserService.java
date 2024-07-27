package com.webbats.payment.broker.module.user.service;

import com.webbats.payment.broker.common.entity.User;
import com.webbats.payment.broker.common.response.PageResponse;
import com.webbats.payment.broker.module.user.request.BrokerUserCreateRequestDto;
import com.webbats.payment.broker.module.user.request.BrokerUserFilterCriteria;
import com.webbats.payment.broker.module.user.request.BrokerUserUpdateRequest;
import com.webbats.payment.broker.module.user.response.BrokerUserPageResponseDto;
import com.webbats.payment.broker.module.user.response.BrokerUserResponseDTO;
import org.springframework.transaction.annotation.Transactional;

public interface BrokerUserService {
    @Transactional
    BrokerUserResponseDTO createBrokerUser(BrokerUserCreateRequestDto request);

    PageResponse<BrokerUserPageResponseDto> getBrokerUsers(BrokerUserFilterCriteria brokerUserFilterCriteria);

    BrokerUserResponseDTO getBrokerUserById(Long userId);

    @Transactional
    BrokerUserResponseDTO updateBrokerUser(BrokerUserUpdateRequest request, Long userId, User loggedInUser);

    void changeBrokerUserPassword(Long userId, User user);
}
