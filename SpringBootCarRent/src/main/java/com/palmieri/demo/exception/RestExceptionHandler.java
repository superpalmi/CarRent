package com.palmieri.demo.exception;

import org.hibernate.hql.internal.ast.ErrorReporter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponse> exceptionNotFoundHandler(Exception ex)
    {
        ErrorResponse error=new ErrorResponse();
        error.setCodice(HttpStatus.NOT_FOUND.value());
        error.setMessaggio(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BindingException.class)
    public ResponseEntity<ErrorResponse> exceptionBindingHandler(Exception ex){
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.BAD_REQUEST.value());
        error.setMessaggio(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> exceptionDuplicateRecordHandler(Exception ex){
        ErrorResponse error = new ErrorResponse();
        error.setCodice(HttpStatus.NOT_ACCEPTABLE.value());
        error.setMessaggio(ex.getMessage());
        return new ResponseEntity<ErrorResponse>(error, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }

}
