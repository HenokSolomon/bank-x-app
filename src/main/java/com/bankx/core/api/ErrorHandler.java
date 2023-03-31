package com.bankx.core.api;

import com.bankx.core.api.model.ErrorResponse;
import com.bankx.core.domain.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@ControllerAdvice
public class ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger( ErrorHandler.class );

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleServiceError(ServiceException ex, WebRequest request) {

        LOGGER.error( "VIPErrorHandler ErrorHandler handleServiceError ", ex );

        return ErrorResponse.builder()
                .timestamp( LocalDateTime.now().format( DateTimeFormatter.ISO_DATE ) )
                .status( HttpStatus.BAD_REQUEST.value() )
                .error( "bad request , please fix your request and try again" )
                .message( ex.getMessage() )
                .path( ((ServletWebRequest) request).getRequest().getRequestURI() )
                .build();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleUnknownError(Exception ex, WebRequest request) {

        LOGGER.error( "VIPErrorHandler ErrorHandler handleUnknownError ", ex );

        return ErrorResponse.builder()
                .timestamp( LocalDateTime.now().format( DateTimeFormatter.ISO_DATE ) )
                .status( HttpStatus.INTERNAL_SERVER_ERROR.value() )
                .error( "Unknown internal server error" )
                .message( "sorry we encountered unknown issue while processing your request , please try again latter" )
                .path( ((ServletWebRequest) request).getRequest().getRequestURI() )
                .build();
    }

}
