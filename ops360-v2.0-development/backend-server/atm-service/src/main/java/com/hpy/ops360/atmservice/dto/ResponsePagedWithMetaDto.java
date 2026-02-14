package com.hpy.ops360.atmservice.dto;
import java.time.LocalDateTime;
import java.util.List;

import com.hpy.rest.dto.IResponseDto;

public class ResponsePagedWithMetaDto<T> implements IResponseDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int responseCode;
    private String message;
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;
    private String error;
    private LocalDateTime timeStamp;
    
    // Constructors
    public ResponsePagedWithMetaDto() {}
    
    // Getters and Setters
    public int getResponseCode() { return responseCode; }
    public void setResponseCode(int responseCode) { this.responseCode = responseCode; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }
    
    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
    
    public boolean isEmpty() { return empty; }
    public void setEmpty(boolean empty) { this.empty = empty; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }
}