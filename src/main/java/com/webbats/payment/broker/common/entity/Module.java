package com.webbats.payment.broker.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "module")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Module {

    @Id
    @Column(name = "module_code", nullable = false)
    private String moduleCode;

    @Column(name = "module_name", nullable = false)
    private String moduleName;

    @Column(name = "module_desc")
    private String moduleDesc;

    @Column(name = "display_order")
    private Integer displayOrder;

    @OneToMany
    @JoinColumn(name = "module_code", referencedColumnName = "module_code")
    private Set<Authority> authorities;
}