package com.finance.accounts.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Global exception handler for the application.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * Error response class.
   */
  private record ErrorResponse(String message, LocalDateTime timestamp, String details) {
  }

  /**
   * Handles AccountNotFoundException.
   *
   * @param ex the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAccountNotFoundException(
      AccountNotFoundException ex, WebRequest request) {
    log.error("Account not found: {}", ex.getMessage());
    
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getMessage(),
        LocalDateTime.now(),
        request.getDescription(false)
    );
    
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles AccountTypeNotFoundException.
   *
   * @param ex the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(AccountTypeNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAccountTypeNotFoundException(
      AccountTypeNotFoundException ex, WebRequest request) {
    log.error("Account type not found: {}", ex.getMessage());
    
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getMessage(),
        LocalDateTime.now(),
        request.getDescription(false)
    );
    
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * Handles validation exceptions.
   *
   * @param ex the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {
    log.error("Validation error: {}", ex.getMessage());
    
    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.toList());
    
    Map<String, Object> response = new HashMap<>();
    response.put("timestamp", LocalDateTime.now());
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("errors", errors);
    response.put("path", request.getDescription(false));
    
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles IllegalArgumentException.
   *
   * @param ex the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex, WebRequest request) {
    log.error("Illegal argument: {}", ex.getMessage());
    
    ErrorResponse errorResponse = new ErrorResponse(
        ex.getMessage(),
        LocalDateTime.now(),
        request.getDescription(false)
    );
    
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles all other exceptions.
   *
   * @param ex the exception
   * @param request the web request
   * @return the error response
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(
      Exception ex, WebRequest request) {
    log.error("Unhandled exception: ", ex);
    
    ErrorResponse errorResponse = new ErrorResponse(
        "An unexpected error occurred",
        LocalDateTime.now(),
        request.getDescription(false)
    );
    
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
