package com.mx.grp.porcentaje_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperacionRequest {

	@NotBlank(message = "Campo num1 es obligatorio")
	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "El número debe ser un valor entero o decimal válido.")
	private String num1;
	
	@NotBlank(message = "Campo num2 es obligatorio")
	@Pattern(regexp = "^\\d+(\\.\\d+)?$", message = "El número debe ser un valor entero o decimal válido.")
	private String num2;
	
}
