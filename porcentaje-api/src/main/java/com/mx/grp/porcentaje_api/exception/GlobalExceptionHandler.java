package com.mx.grp.porcentaje_api.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(PorcentajeNoDisponibleException.class)
	public ResponseEntity<ErrorResponse> handlePorcentajeException(PorcentajeNoDisponibleException ex,
			HttpServletRequest request) {
		HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), status.value(), "Porcentaje no disponible",
				ex.getMessage(), request.getRequestURI()
		);

		return ResponseEntity.status(status).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
		
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), status.value(), "Error interno",
				ex.getMessage(), request.getRequestURI()
		);

		return ResponseEntity.status(status).body(errorResponse);
	}

}
