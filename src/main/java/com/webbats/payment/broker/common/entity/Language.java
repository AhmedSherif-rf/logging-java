package com.webbats.payment.broker.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "languages")
@Data
@NoArgsConstructor
public class Language {
    @Id
    @Column(name = "lang_code", length = 10, unique = true, nullable = false)
    private String langCode;
    private String langName;
}
