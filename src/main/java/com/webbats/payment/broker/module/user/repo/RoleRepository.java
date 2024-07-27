package com.webbats.payment.broker.module.user.repo;

import com.webbats.payment.broker.common.entity.Role;
import com.webbats.payment.broker.common.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Set<Role> findByIdInAndUserTypeCode(Set<String> names, String userTypeCode);

    Set<Role> findByUserTypeCode(UserType userTypeCode);

    Optional<Role> findByName(String name);

}