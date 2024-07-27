package com.webbats.payment.broker.common.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currency")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency extends AuditEntity {

    @Id
    @Column(name = "currency_code", nullable = false, unique = true)
    private String currencyCode;

    @Column(name = "currency_name", nullable = false, unique = true)
    private String currencyName;

    @Column(name = "is_active")
    private boolean isActive;
}
