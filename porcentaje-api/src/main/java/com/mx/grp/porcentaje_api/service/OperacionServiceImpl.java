package com.mx.grp.porcentaje_api.service;

import java.util.Map;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.grp.porcentaje_api.dto.request.OperacionRequest;
import com.mx.grp.porcentaje_api.dto.response.OperacionResponse;
import com.mx.grp.porcentaje_api.exception.PorcentajeNoDisponibleException;
import com.mx.grp.porcentaje_api.service.externo_mock.PorcentajeServiceMock;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class OperacionServiceImpl implements OperacionService {

	private final PorcentajeServiceMock porcentajeServiceMock;
	private final CacheManager cacheManager;
	private final HistorialOperacionService historialOperacionService;
	private final ObjectMapper objectMapper;

	public OperacionServiceImpl(PorcentajeServiceMock porcentajeServiceMock, CacheManager cacheManager,
			HistorialOperacionService historialOperacionService, ObjectMapper objectMapper) {
		this.porcentajeServiceMock = porcentajeServiceMock;
		this.cacheManager = cacheManager;
		this.historialOperacionService = historialOperacionService;
		this.objectMapper = objectMapper;
	}

	@Override
	public OperacionResponse calcularConAuditoria(OperacionRequest request, HttpServletRequest httpRequest)
			throws JsonProcessingException {
		String endpoint = httpRequest.getRequestURI();

		try {
			String requestJson = objectMapper.writeValueAsString(request);
			OperacionResponse response = calcularOperacion(request);
			String responseJson = objectMapper.writeValueAsString(response);

			historialOperacionService.guardar(endpoint, requestJson, responseJson, false);
			return response;
		} catch (Exception e) {
			String requestJson = objectMapper.writeValueAsString(request);
			String errorJson = objectMapper.writeValueAsString(Map.of("error", e.getMessage()));

			historialOperacionService.guardar(endpoint, requestJson, errorJson, true);
			throw e; // Dejamos que el manejador global de excepciones devuelva el error apropiado.
		}
	}

	OperacionResponse calcularOperacion(OperacionRequest request) {
		double num1 = Double.parseDouble(request.getNum1());
		double num2 = Double.parseDouble(request.getNum2());
		double suma = num1 + num2;
		double porcentaje;
		try {
			porcentaje = porcentajeServiceMock.obtenerPorcentaje();
		} catch (Exception ex) {
			// Intenta obtener del caché manualmente si el servicio falla.
			Double cacheado = obtenerPorcentajeDesdeCaché();
			if (cacheado != null) {
				porcentaje = cacheado;
			} else {
				throw new PorcentajeNoDisponibleException(
						"No se pudo obtener el porcentaje ni del servicio ni del caché");
			}
		}

		double resultado = suma + (suma * porcentaje / 100);
		return new OperacionResponse(resultado, porcentaje);
	}

	Double obtenerPorcentajeDesdeCaché() {
		Cache cache = cacheManager.getCache("porcentaje");
	    if (cache == null || cache.get(SimpleKey.EMPTY, Double.class) == null) {
	        return null; 
	    }
	    return cache.get(SimpleKey.EMPTY, Double.class);
	}

}
