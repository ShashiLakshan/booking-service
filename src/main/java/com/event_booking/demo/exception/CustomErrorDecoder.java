package com.event_booking.demo.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new CustomGlobalException("EVENT_NOT_FOUND", "Event not found", HttpStatus.NOT_FOUND);
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}