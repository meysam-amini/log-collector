package com.meysam.logcollector.querylogs.model;

import lombok.Data;

@Data
public class QueryLogsDto {
    private String txt;
    private int pageNo;
    private int pageSize;
}
