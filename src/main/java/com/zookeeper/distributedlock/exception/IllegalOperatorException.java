package com.zookeeper.distributedlock.exception;

public class IllegalOperatorException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public IllegalOperatorException(String message) {
        super(message);
    }
    
}
