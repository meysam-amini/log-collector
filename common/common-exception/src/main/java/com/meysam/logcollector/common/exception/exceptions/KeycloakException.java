package com.meysam.logcollector.common.exception.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class KeycloakException extends RuntimeException{

    private HttpStatusCode httpStatusCode;

    public KeycloakException(HttpStatusCode httpStatusCode,String message){
        super(message);
        this.httpStatusCode=httpStatusCode;
    }
}
