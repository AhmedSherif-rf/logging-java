package com.webbats.payment.broker.common.repo;

import com.webbats.payment.broker.common.entity.Role;
import com.webbats.payment.broker.common.entity.User;
 import com.webbats.payment.broker.common.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndUserType(String email, UserType userType);

    List<User> findByRoles(Role role);

    Optional<User> findByIdAndUserType(Long id, UserType userType);
}