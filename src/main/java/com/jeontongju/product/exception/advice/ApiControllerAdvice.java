package com.jeontongju.product.exception.advice;

import com.jeontongju.product.exception.common.DomainException;
import com.jeontongju.product.dto.temp.ErrorFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiControllerAdvice extends ResponseEntityExceptionHandler {

  private static final String UNIQUE_CONSTRAINT_EXCEPTION_MESSAGE = "유니크 제약조건 오류 ";
  private static final String DUPLICATE_KEY_EXCEPTION_MESSAGE = "중복 키 오류 ";

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErrorFormat> domainException(DomainException e) {
    log.error("{PRODUCT}", e.getMessage());
    HttpStatus status = e.getStatus();
    ErrorFormat body =
        ErrorFormat.builder()
            .code(status.value())
            .message(status.name())
            .detail(e.getMessage())
            .build();

    return ResponseEntity.status(status.value()).body(body);
  }

  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<ErrorFormat> duplicateKeyException(DuplicateKeyException e) {
    log.error("{PRODUCT}", e.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorFormat body =
        ErrorFormat.builder()
            .code(status.value())
            .message(status.name())
            .detail(DUPLICATE_KEY_EXCEPTION_MESSAGE)
            .build();

    return ResponseEntity.status(status.value()).body(body);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    log.error("{PRODUCT}", e.getMessage());
    ErrorFormat body =
        ErrorFormat.builder()
            .code(status.value())
            .message(status.name())
            .detail(
                e.getBindingResult().getFieldError() == null
                    ? e.getMessage()
                    : e.getBindingResult().getFieldError().getDefaultMessage())
            .build();

    return ResponseEntity.status(status.value()).body(body);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorFormat> constraintViolationException(
      DataIntegrityViolationException e) {
    log.error("{PRODUCT}", e.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ErrorFormat body =
        ErrorFormat.builder()
            .code(status.value())
            .message(status.name())
            .detail(UNIQUE_CONSTRAINT_EXCEPTION_MESSAGE)
            .build();

    return ResponseEntity.status(status.value()).body(body);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorFormat> exception(Exception e) {
    log.error("{PRODUCT}", e.getMessage());
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    ErrorFormat body =
        ErrorFormat.builder()
            .code(status.value())
            .message(status.name())
            .detail(e.getMessage())
            .build();

    return ResponseEntity.status(status.value()).body(body);
  }
}
