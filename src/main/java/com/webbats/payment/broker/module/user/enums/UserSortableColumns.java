package com.webbats.payment.broker.module.user.enums;

import lombok.Getter;

@Getter
public enum UserSortableColumns {
    email("email"),
    country("country.countryCode"),
    status("status"),
    createdBy("createdBy"),
    createdAt("createdAt");

    private final String fieldName;

    UserSortableColumns(String fieldName) {
        this.fieldName = fieldName;
    }

    public static String getEntityFieldName(String value) {
        for (UserSortableColumns column : values()) {
            if (column.name().equalsIgnoreCase(value)) {
                return column.getFieldName();
            }
        }
        return null;
    }
}
