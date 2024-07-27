package com.webbats.payment.broker.common.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponse<T> {
    private long totalElements;
    private int pageNumber;
    private int pageSize;
    private List<T> content;
}