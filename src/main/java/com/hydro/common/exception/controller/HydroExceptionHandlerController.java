package com.hydro.common.exception.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hydro.common.exception.BaseException;
import com.hydro.common.exception.InsufficientPermissionsException;
import com.hydro.common.exception.InvalidCredentialsException;
import com.hydro.common.exception.JwtTokenException;
import com.hydro.common.exception.NotFoundException;
import com.hydro.common.exception.domain.ExceptionError;

/**
 * Exception Helper class for returning response entitys of the errored objects.
 * 
 * @author Sam Butler
 * @since August 24, 2021
 */
@RestControllerAdvice
public class HydroExceptionHandlerController extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HydroExceptionHandlerController.class);

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionError handleInvalidCredentialsException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ExceptionError(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionError handleNotFoundException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ExceptionError(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object handleJwtTokenException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ExceptionError(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InsufficientPermissionsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionError handleInsufficientPermissionsException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ExceptionError(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionError handleBaseException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ExceptionError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionError handleException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ExceptionError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
