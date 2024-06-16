package hr.xmjosic.product.controller;

import hr.xmjosic.product.constant.ConstraintMessage;
import hr.xmjosic.product.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Global controller advice. */
@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

  /**
   * Handles the IllegalArgumentException and returns an ErrorDto with the appropriate details.
   *
   * @param ex the IllegalArgumentException that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ErrorDto handleIllegalArgumentException(
      IllegalArgumentException ex, HttpServletRequest request) {
    return ErrorDto.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(ex.getClass().getName())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
  }

  /**
   * Handles the DataIntegrityViolation exception and returns an ErrorDto with the appropriate
   * details.
   *
   * @param ex the DataIntegrityViolation exception that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorDto> handleDataIntegrityViolationException(
      ConstraintViolationException ex, HttpServletRequest request) {
    log.error("Error occurred: {}", ex.getMessage(), ex);
    String message = ConstraintMessage.getMessageByConstraintName(ex.getConstraintName());
    HttpStatus status =
        StringUtils.hasText(message) ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
    return ResponseEntity.status(status)
        .body(
            ErrorDto.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(ex.getClass().getName())
                .message(
                    StringUtils.hasText(message)
                        ? message
                        : "Constraint violation exception occurred.")
                .path(request.getRequestURI())
                .build());
  }

  /**
   * Handles the MethodArgumentNotValid exception and returns an ErrorDto with the appropriate
   * details.
   *
   * @param ex the MethodArgumentNotValid exception that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorDto handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    log.error("Error occurred: {}", ex.getMessage(), ex);
    List<String> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(s -> s.getField() + ": " + s.getDefaultMessage())
            .collect(Collectors.toList());
    return ErrorDto.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(ex.getClass().getName())
        .message(String.join(";\n", errors))
        .path(request.getRequestURI())
        .build();
  }

  /**
   * Handles the Throwable exception and returns an ErrorDto with the appropriate details.
   *
   * @param ex the Throwable exception that was thrown
   * @param request the HttpServletRequest object representing the current request
   * @return an ErrorDto object containing the timestamp, status code, error class name, error
   *     message, and request path
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Throwable.class)
  public ErrorDto handleThrowable(Throwable ex, HttpServletRequest request) {
    log.error("Error occurred: {}", ex.getMessage(), ex);
    return ErrorDto.builder()
        .timestamp(Instant.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(ex.getClass().getName())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();
  }
}
