package com.meysam.logcollector.common.service.feign.components;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

//@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String s, Response response) {
        Exception exception = defaultErrorDecoder.decode(s, response);

//        if(exception instanceof RetryableException){
//            return exception;
//        }


        if(response.status() == 500){
            return new RetryableException(response.status(),"500 Error!",response.request().httpMethod(),null,400L,response.request());
        }
        if(response.status() == 504){
            return new RetryableException(response.status(),"504 Error!",response.request().httpMethod(),null,400L,response.request());
        }
        if(response.status() == 401){
            return new RetryableException(response.status(),"401 Error!",response.request().httpMethod(),null,400L,response.request());
        }
        if(response.status() == 403){
            return new RetryableException(response.status(),"403 Error!",response.request().httpMethod(),null,400L,response.request());
        }
        if(response.status() == -1){
            return new RetryableException(response.status(),"Service Unavailable!",response.request().httpMethod(),null,400L,response.request());
        }

        return exception;
    }
}