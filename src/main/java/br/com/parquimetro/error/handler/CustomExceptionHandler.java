package br.com.parquimetro.error.handler;

import br.com.parquimetro.error.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> InternalServerError(Exception ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getTimestamp(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> DocumentNotFound(DocumentNotFoundException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getTimestamp(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public final ResponseEntity<ExceptionResponse> duplicateEmail(DuplicateEmailException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getTimestamp(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(StatusException.class)
    public final ResponseEntity<ExceptionResponse> statusError(StatusException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getTimestamp(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(MetodoPagamentoException.class)
    public final ResponseEntity<ExceptionResponse> metodoPagamentoError(MetodoPagamentoException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getTimestamp(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(EstacionamentoException.class)
    public final ResponseEntity<ExceptionResponse> estacionamentoError(EstacionamentoException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getTimestamp(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(TransactionException.class)
    public final ResponseEntity<ExceptionResponse> transactionError(TransactionException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getTimestamp(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public final ResponseEntity<ExceptionResponse> optimisticLockingFailure(OptimisticLockingFailureException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getTimestamp(), HttpStatus.CONFLICT.value(),"conflito na atualização do documento, por favor, tente novamente mais tarde", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ExceptionResponse> httpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getTimestamp(), HttpStatus.BAD_REQUEST.value(), "requisição sem corpo", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponseValidation> methodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ExceptionResponseValidation exceptionResponseValidation = new ExceptionResponseValidation(getTimestamp(), HttpStatus.UNPROCESSABLE_ENTITY.value(), "Existem dados incorretos no corpo da requisição", request.getRequestURI());
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> exceptionResponseValidation.addError(fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exceptionResponseValidation);
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

}
