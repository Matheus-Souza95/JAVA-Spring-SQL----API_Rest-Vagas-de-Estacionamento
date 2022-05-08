package com.api.parkingcontrol.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

//The @ControllerAdvice annotation is specialization of @Component annotation so that it is auto-detected via classpath scanning.
// A Controller Advice is a kind of interceptor that surrounds the logic in our Controllers and allows us to apply some common logic to them.

//Its methods (annotated with @ExceptionHandler) are shared globally across multiple @Controller components to capture exceptions and translate them to HTTP responses.
//The ExceptionHandler annotation indicates which type of Exception we want to handle. The exception instance and the request will be injected via method arguments.
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setMessage(ex.getMessage());
        message.setDescription("Recurso nao encontrado");
        message.setStatusCode(HttpStatus.NOT_FOUND.value());
        message.setPath(request.getContextPath());

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setMessage(ex.getFieldError().getDefaultMessage());
        message.setDescription("Campo invalido");
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        message.setPath(request.getContextPath());

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.valueOf(message.getStatusCode()));
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorMessage> alreadyExistException(AlreadyExistException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setMessage(ex.getMessage());
        message.setDescription("Esse recurso ja existe");
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        message.setPath(request.getContextPath());

        return new ResponseEntity<ErrorMessage>(message, HttpStatus.valueOf(message.getStatusCode()));
    }
}
