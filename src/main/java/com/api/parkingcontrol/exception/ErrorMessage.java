package com.api.parkingcontrol.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    private String path;

    public ErrorMessage() {
        this.timestamp = new Date();
    }
}