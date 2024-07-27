package com.webbats.payment.broker.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "country")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country extends AuditEntity {

    @Id
    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_name", nullable = false, unique = true)
    private String countryName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_code", referencedColumnName = "currency_code")
    private Currency currency;

    @Column(name = "cort")
    private Integer cort;

    @Column(name = "ttp_volume_threshold")
    private Double ttpVolumeThreshold;

    @Column(name = "ttp_orders_threshold")
    private Double ttpOrdersThreshold;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

}