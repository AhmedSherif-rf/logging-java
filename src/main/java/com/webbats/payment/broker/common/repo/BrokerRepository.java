package com.webbats.payment.broker.common.repo;


import com.webbats.payment.broker.common.entity.Broker;
import com.webbats.payment.broker.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Long> {


    Optional<Broker> findByUser(User user);
}
