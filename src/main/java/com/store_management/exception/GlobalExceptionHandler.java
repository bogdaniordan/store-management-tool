package com.store_management.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.bad_request}")
    private String errorCodeBadRequest;

    @Value("${spring.application.forbidden}")
    private String errorCodeForbidden;

    @Value("${spring.application.not_found}")
    private String errorCodeNotFound;

    @Value("${spring.application.un_authorized}")
    private String errorCodeUnauthorized;

    @Value("${spring.application.server_error}")
    private String errorCodeInternalServerError;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        logger.error("Resource not found exception: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Resource not found", errorCodeNotFound, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InventoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleInventoryAlreadyExistsException(InventoryAlreadyExistsException e) {
        logger.error("Inventory already exists exception: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Inventory already exists", errorCodeBadRequest, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        logger.error("User already exists exception: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("User already exists", errorCodeBadRequest, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        logger.error("Bad credentials when trying to authenticate: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Bad credentials when authenticating", errorCodeUnauthorized, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> handleUserDoesNotExistException(UserDoesNotExistException e) {
        logger.error("User does not exist exception: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("User does not exist", errorCodeBadRequest, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        logger.error("Authorization denied exception: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Authorization denied", errorCodeForbidden, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        Throwable cause = ((TransactionSystemException) e).getRootCause();
        if (cause instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
            List<String> errors = constraintViolations.stream().map(ConstraintViolation::getMessage).toList();
            logger.error("Constraint violation exception {}", errors);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        } else {
            logger.error("Exception: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error", errorCodeInternalServerError, e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
