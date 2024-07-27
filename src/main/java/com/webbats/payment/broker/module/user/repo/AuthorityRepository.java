package com.webbats.payment.broker.module.user.repo;

import com.webbats.payment.broker.common.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}