package com.webbats.payment.broker.module.user.request;

import com.webbats.payment.broker.common.enums.UserStatus;
import com.webbats.payment.broker.common.request.PaginationRequest;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BrokerUserFilterCriteria extends PaginationRequest {
    private String roleId;
    private String countryCode;
    private UserStatus status;
    private String createdBy;
    private String language;
}