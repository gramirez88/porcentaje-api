package com.mx.grp.porcentaje_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mx.grp.porcentaje_api.dto.request.OperacionRequest;
import com.mx.grp.porcentaje_api.dto.response.OperacionResponse;
import com.mx.grp.porcentaje_api.service.OperacionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/operacion")
public class OperacionController {

	private final OperacionService operacionService;

	public OperacionController(OperacionService operacionService) {
		this.operacionService = operacionService;
	}

	@PostMapping("/calcular")
	public ResponseEntity<OperacionResponse> calcular(@Valid @RequestBody OperacionRequest request,
			HttpServletRequest httpRequest) throws JsonProcessingException {

		OperacionResponse response = operacionService.calcularConAuditoria(request, httpRequest);
		  // Imprimir para ver qué respuesta está devolviendo
	    System.out.println("Respuesta desde el controlador: " + response);
	    
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}
