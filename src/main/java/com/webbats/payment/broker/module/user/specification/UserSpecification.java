package com.webbats.payment.broker.module.user.specification;

import com.webbats.payment.broker.common.entity.Country;
import com.webbats.payment.broker.common.entity.Language;
import com.webbats.payment.broker.common.entity.Role;
import com.webbats.payment.broker.common.entity.User;
import com.webbats.payment.broker.common.enums.UserType;
import com.webbats.payment.broker.module.user.request.BrokerUserFilterCriteria;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class UserSpecification {

    public static Specification<User> getUsersByFilterCriteria(BrokerUserFilterCriteria filterCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(filterCriteria.getRoleId())) {
                Join<User, Role> roleJoin = root.join("roles", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(roleJoin.get("id"), filterCriteria.getRoleId()));
            }

            if (StringUtils.hasText(filterCriteria.getCountryCode())) {
                Join<User, Country> countryJoin = root.join("country", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(countryJoin.get("countryCode"), filterCriteria.getCountryCode()));
            }

            // Check for user status
            if (filterCriteria.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filterCriteria.getStatus()));
            }

            // Check for created by
            if (StringUtils.hasText(filterCriteria.getCreatedBy())) {
                predicates.add(criteriaBuilder.equal(root.get("createdBy"), filterCriteria.getCreatedBy()));
            }

            // Check for language
            if (StringUtils.hasText(filterCriteria.getLanguage())) {
                Join<User, Language> languageJoin = root.join("languages", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(languageJoin.get("langCode"), filterCriteria.getLanguage()));
            }

            predicates.add(criteriaBuilder.equal(root.get("userType"), UserType.BROKER));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
