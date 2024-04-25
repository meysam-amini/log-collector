package com.meysam.logcollector.common.outbox.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PaginationUtils {

    public static Pageable getPageable(int inputPageNo, int inputPageSize) {
        return PageRequest.of(inputPageNo,inputPageSize);
    }

    public static Pageable getFirstPagePageable( int inputPageSize) {
        return getPageable(0, inputPageSize);
    }
}
