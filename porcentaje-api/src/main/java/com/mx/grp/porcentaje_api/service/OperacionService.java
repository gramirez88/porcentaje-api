package com.mx.grp.porcentaje_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mx.grp.porcentaje_api.dto.request.OperacionRequest;
import com.mx.grp.porcentaje_api.dto.response.OperacionResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface OperacionService {

	//OperacionResponse calcularOperacion(OperacionRequest request);
	
	OperacionResponse calcularConAuditoria(OperacionRequest request, HttpServletRequest httpRequest) throws JsonProcessingException;
}
