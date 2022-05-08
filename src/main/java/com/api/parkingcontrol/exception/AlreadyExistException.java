package com.api.parkingcontrol.exception;

public class AlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AlreadyExistException(String msg) {
        super(msg);
    }

    public AlreadyExistException() {
    }
}
