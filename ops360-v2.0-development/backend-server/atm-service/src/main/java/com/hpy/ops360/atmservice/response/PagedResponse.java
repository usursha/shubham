package com.hpy.ops360.atmservice.response;

import java.util.List;

import com.hpy.rest.dto.IResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PagedResponse<T> implements IResponseDto{
    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private boolean empty;
    
    public PagedResponse() {}
    
    public PagedResponse(List<T> data, int page, int size, long totalElements) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        this.last = page >= Math.max(0, totalPages - 1);
        this.first = page == 0;
        this.empty = data == null || data.isEmpty();
    }
    
    // Getters and Setters
    public List<T> getContent() { return data; }
    public void setContent(List<T> data) { this.data = data; }
    
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
    
    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }
    
    public boolean isEmpty() { return empty; }
    public void setEmpty(boolean empty) { this.empty = empty; }

}