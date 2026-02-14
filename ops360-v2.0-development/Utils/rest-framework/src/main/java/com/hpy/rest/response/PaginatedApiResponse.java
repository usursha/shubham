package com.hpy.rest.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedApiResponse<T, Pagination> {
    private int status;
    private String message;
    private T data;
    private Pagination pagination;
}

