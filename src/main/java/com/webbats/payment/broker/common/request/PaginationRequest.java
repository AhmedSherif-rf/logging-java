package com.webbats.payment.broker.common.request;

import com.webbats.payment.broker.common.exception.ServiceRuntimeException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.function.UnaryOperator;

@Getter
@Setter
public class PaginationRequest {

    private int pageNumber = 0;
    private int pageSize = 10;
    private String sortColumn;
    private SortDirection sortDirection = SortDirection.ASC;

    public PageRequest getPageRequest() {
        return PageRequest.of(getPageNumber(), getPageSize());
    }

    public PageRequest getSortablePageRequest(UnaryOperator<String> fieldName) {
        if (sortColumn != null && !sortColumn.isEmpty()) {
            if (StringUtils.isEmpty(fieldName.apply(sortColumn))) {
                throw new ServiceRuntimeException(String.format("Sort column %s not supported", sortColumn));
            }
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.name());
            Sort sort = Sort.by(direction, fieldName.apply(sortColumn));
            return PageRequest.of(getPageNumber(), getPageSize(), sort);
        } else {
            return PageRequest.of(getPageNumber(), getPageSize());
        }
    }

    private enum SortDirection {
        ASC,
        DESC
    }
}
