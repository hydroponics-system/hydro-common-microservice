package com.hydro.common.exception.exceptions;

/**
 * Exception thrown when exception occurs for jwt.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
public class JwtTokenException extends BaseException {
    public JwtTokenException(String msg) {
        super(msg);
    }
}