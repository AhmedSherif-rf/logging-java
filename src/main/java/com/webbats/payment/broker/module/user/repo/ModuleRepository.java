package com.webbats.payment.broker.module.user.repo;

import com.webbats.payment.broker.common.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, String> {
    List<Module> findAllByOrderByDisplayOrderAsc();
}
