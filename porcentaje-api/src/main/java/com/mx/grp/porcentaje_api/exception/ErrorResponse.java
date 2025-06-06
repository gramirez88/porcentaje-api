package com.mx.grp.porcentaje_api.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private LocalDateTime timestamp;
	private int statusCode;
    private String error;
    private String message;
    private String path;
}
