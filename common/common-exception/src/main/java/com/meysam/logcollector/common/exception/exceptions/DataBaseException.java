package com.meysam.logcollector.common.exception.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class DataBaseException extends RuntimeException{

    public DataBaseException(String message){
        super(message);
    }
}
