package com.mx.grp.porcentaje_api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import com.mx.grp.porcentaje_api.dto.response.HistorialPageResponse;
import com.mx.grp.porcentaje_api.dto.response.HistorialResponse;
import com.mx.grp.porcentaje_api.service.HistorialOperacionService;

@WebMvcTest(HistorialOperacionController.class)
class HistorialOperacionControllerTest {

	 @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private HistorialOperacionService historialService;
	    
	    @Test
	    void obtenerHistorialDebeRetornarListaDeHistorial() throws Exception {
	        // Arrange
	        Pageable pageable = PageRequest.of(0, 10);
	        HistorialResponse historial = new HistorialResponse(
	        		LocalDateTime.of(2024, 5, 1, 10, 0),
	                "/api/v1/operacion/calcular",
	                "{\"valor1\": \"10\", \"valor2\": \"5\"}",
	                "15.5",
	                false
	        );
	        List<HistorialResponse> contenido = Collections.singletonList(historial);
	        HistorialPageResponse response = new HistorialPageResponse(contenido, 1, 1L);

	        when(historialService.obtenerHistorial(any(Pageable.class))).thenReturn(response);

	        // Act & Assert
	        mockMvc.perform(get("/api/v1/historial")
	                .param("page", "0")
	                .param("size", "10"))
	                .andExpect(status().isOk())
	                .andExpect(jsonPath("$.items[0].endpoint").value("/api/v1/operacion/calcular"))
	                .andExpect(jsonPath("$.items[0].resultado").value("15.5"))
	                .andExpect(jsonPath("$.items[0].error").value(false));
	    }

}
