package com.ringkhang.freewill.exception;


public class RequestedResourceNotAvailable extends RuntimeException {
    public RequestedResourceNotAvailable(String message) {
        super(message);
    }
}
