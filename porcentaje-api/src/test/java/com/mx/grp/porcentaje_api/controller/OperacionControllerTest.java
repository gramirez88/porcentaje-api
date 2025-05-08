package com.mx.grp.porcentaje_api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.grp.porcentaje_api.dto.request.OperacionRequest;
import com.mx.grp.porcentaje_api.dto.response.OperacionResponse;
import com.mx.grp.porcentaje_api.service.OperacionService;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(OperacionController.class)
class OperacionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OperacionService operacionService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void calcularConAuditoriaDebeDevolverResultadoCorrecto() throws Exception {
		// Arrange
		OperacionRequest request = new OperacionRequest("10", "5");
		OperacionResponse response = new OperacionResponse(16.5, 10.0); // resultado esperado

		when(operacionService.calcularConAuditoria(eq(request), any(HttpServletRequest.class))).thenReturn(response);

		// Act & Assert
		mockMvc.perform(post("/api/v1/operacion/calcular").contentType("application/json")
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk()) // Verificamos que la
																								// respuesta sea OK
																								// (200)
				.andExpect(jsonPath("$.resultado").value(16.5)) // Verificamos el valor del resultado
				.andExpect(jsonPath("$.porcentajeAplicado").value(10.0));// Verificamos el porcentaje aplicado
	}
}
