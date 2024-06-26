package com.meysam.logcollector.common.exception.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class BusinessException extends RuntimeException{

    private HttpStatusCode httpStatusCode;

    public BusinessException(String message){
        super(message);
    }

    public BusinessException(HttpStatusCode httpStatusCode,String message){
        super(message);
        this.httpStatusCode=httpStatusCode;
    }
}
