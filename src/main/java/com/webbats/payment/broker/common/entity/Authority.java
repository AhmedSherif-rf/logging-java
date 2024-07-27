package com.webbats.payment.broker.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "authority")
@Getter
@Setter
public class Authority {
    @Id
    @Column(name = "authority_code")
    private String authorityCode;

    @Column(name = "authority_name")
    private String authorityName;

    @Column(name = "authority_desc")
    private String authorityDesc;

    @ManyToOne
    @JoinColumn(name = "module_code", referencedColumnName = "module_code")
    private Module module;
}