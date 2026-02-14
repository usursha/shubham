package com.hpy.ops360.atmservice.response;
import java.time.LocalDateTime;
import com.hpy.rest.dto.IResponseDto;

public class ResponsePagedDto<T> implements IResponseDto {
    private int responseCode;
    private String message;
    private PagedResponse<T> data;
    private String error;
    private LocalDateTime timeStamp;
    
    // Default constructor
    public ResponsePagedDto() {}
    
    // Getters and Setters
    public int getResponseCode() { 
        return responseCode; 
    }
    
    public void setResponseCode(int responseCode) { 
        this.responseCode = responseCode; 
    }
    
    public String getMessage() { 
        return message; 
    }
    
    public void setMessage(String message) { 
        this.message = message; 
    }
    
    public PagedResponse<T> getData() { 
        return data; 
    }
    
    public void setData(PagedResponse<T> data) { 
        this.data = data; 
    }
    
    public String getError() { 
        return error; 
    }
    
    public void setError(String error) { 
        this.error = error; 
    }
    
    public LocalDateTime getTimeStamp() { 
        return timeStamp; 
    }
    
    public void setTimeStamp(LocalDateTime timeStamp) { 
        this.timeStamp = timeStamp; 
    }
}