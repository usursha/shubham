package com.hpy.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private int last;
}
