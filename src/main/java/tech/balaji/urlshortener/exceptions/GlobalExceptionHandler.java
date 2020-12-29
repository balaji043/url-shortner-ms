package tech.balaji.urlshortener.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tech.balaji.urlshortener.response.ApiErrorResponse;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.springframework.core.PriorityOrdered.*;
import static org.springframework.http.ResponseEntity.*;

@ControllerAdvice()
@Slf4j
@Priority(HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {


    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiErrorResponse<String>> handleNotFoundException(HttpServletRequest request, CommonException ex) {
        log.error("CommonException {}\n", request.getRequestURI(), ex);
        return status(ex.getCode())
                .body(new ApiErrorResponse<>(ex.getMessage(), singletonList(ex.getMessage())));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiErrorResponse<String>> handleValidationException(HttpServletRequest request, ValidationException ex) {
        log.error("ValidationException {}\n", request.getRequestURI(), ex);

        return badRequest()
                .body(new ApiErrorResponse<>("Validation exception", singletonList(ex.getMessage())));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse<String>> handleMissingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException {}\n", request.getRequestURI(), ex);

        return badRequest()
                .body(new ApiErrorResponse<>("Missing request parameter", singletonList(ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse<Map<String, String>>> handleMethodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException ex) {
        log.error("handleMethodArgumentTypeMismatchException {}\n", request.getRequestURI(), ex);

        Map<String, String> details = new HashMap<>();
        details.put("paramName", ex.getName());
        details.put("paramValue", ofNullable(ex.getValue()).map(Object::toString).orElse(""));
        details.put("errorMessage", ex.getMessage());

        return badRequest()
                .body(new ApiErrorResponse<>("Method argument type mismatch", singletonList(details)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse<Map<String, String>>> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException {}\n", request.getRequestURI(), ex);

        List<Map<String, String>> details = new ArrayList<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {
                    Map<String, String> detail = new HashMap<>();
                    detail.put("objectName", fieldError.getObjectName());
                    detail.put("field", fieldError.getField());
                    detail.put("rejectedValue", "" + fieldError.getRejectedValue());
                    detail.put("errorMessage", fieldError.getDefaultMessage());
                    details.add(detail);
                });

        return badRequest()
                .body(new ApiErrorResponse<>("Method argument validation failed", details));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse<String>> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex) {
        log.error("handleAccessDeniedException {}\n", request.getRequestURI(), ex);

        return status(HttpStatus.FORBIDDEN)
                .body(new ApiErrorResponse<>("Access denied!", singletonList(ex.getMessage())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse<String>> handleInternalServerError(HttpServletRequest request, Exception ex) {
        log.error("handleInternalServerError {}\n", request.getRequestURI(), ex);

        return status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiErrorResponse<>("Internal server error", singletonList(ex.getMessage())));
    }

}



