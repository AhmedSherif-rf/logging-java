package com.webbats.payment.broker.common.repo;

import com.webbats.payment.broker.common.entity.ValidToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ValidTokenRepository extends JpaRepository<ValidToken, Long> {

    List<ValidToken> findByToken(String token);

    @Query("SELECT count (1) FROM ValidToken vt where vt.token=:token AND vt.user.email=:email AND vt.deviceFingerprint=:deviceFingerPrint ")
    int countByTokenAndUserEmailAAndDeviceFingerprint(String token, String email, String deviceFingerPrint);

    Optional<ValidToken> findByUserId(Long userId);

    @Modifying
    @Transactional
    void deleteByToken(String token);

    @Modifying
    @Transactional
    int deleteTokensByCreatedAtBefore(Date date);

    void deleteByUserId(Long userId);

    @Modifying
    @Query("UPDATE ValidToken vt set vt.deviceFingerprint=:fingerprint where vt.user.id=:userId")
    void updateFingerprintByUserId(Long userId, String fingerprint);
}