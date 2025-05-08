package com.mx.grp.porcentaje_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.grp.porcentaje_api.dto.request.OperacionRequest;
import com.mx.grp.porcentaje_api.dto.response.OperacionResponse;
import com.mx.grp.porcentaje_api.exception.PorcentajeNoDisponibleException;
import com.mx.grp.porcentaje_api.service.externo_mock.PorcentajeServiceMock;

import jakarta.servlet.http.HttpServletRequest;

class OperacionServiceImplTest {
    private PorcentajeServiceMock porcentajeServiceMock;
    private CacheManager cacheManager;
    private HistorialOperacionService historialOperacionService;
    private OperacionServiceImpl operacionService;
    
    ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {
        porcentajeServiceMock = mock(PorcentajeServiceMock.class);
        cacheManager = mock(CacheManager.class);
        historialOperacionService = mock(HistorialOperacionService.class);
        objectMapper = new ObjectMapper();
        operacionService = new OperacionServiceImpl(
                porcentajeServiceMock, cacheManager, historialOperacionService, objectMapper);
    }
    
    @Nested
    @DisplayName("calcularOperacion - comportamiento exitoso")
    class CalculoExitoso {

        @Test
        @DisplayName("Debería calcular correctamente el resultado con porcentaje del servicio externo")
        void debeCalcularResultadoConPorcentajeServicioExterno() {
            // Arrange
            OperacionRequest request = new OperacionRequest("200", "50"); // suma = 250
            double porcentaje = 20.0;
            when(porcentajeServiceMock.obtenerPorcentaje()).thenReturn(porcentaje);

            // Act
            OperacionResponse response = operacionService.calcularOperacion(request);

            // Assert
            double resultadoEsperado = 250 + (250 * 20.0 / 100); // 250 + 50 = 300
            assertEquals(resultadoEsperado, response.getResultado());
            assertEquals(porcentaje, response.getPorcentajeAplicado());
        }
    }
    
    @Nested
    @DisplayName("calcularOperacion - fallback al caché")
    class CalculoConFalloEnServicio {

        @Test
        @DisplayName("Debería usar valor cacheado si el servicio externo falla")
        void debeUsarCacheSiServicioFalla() {
            // Arrange
            OperacionRequest request = new OperacionRequest("100", "100"); // suma = 200

            // Simular fallo del servicio externo
            when(porcentajeServiceMock.obtenerPorcentaje()).thenThrow(new RuntimeException("Servicio caído"));

            // Simular caché
            Cache mockCache = mock(Cache.class);
            when(cacheManager.getCache("porcentaje")).thenReturn(mockCache);
            when(mockCache.get(any(), eq(Double.class))).thenReturn(15.0); // 15% desde caché

            // Act
            OperacionResponse response = operacionService.calcularOperacion(request);

            // Assert
            double resultadoEsperado = 200 + (200 * 15.0 / 100); // 230
            assertEquals(resultadoEsperado, response.getResultado());
            assertEquals(15.0, response.getPorcentajeAplicado());
        }
    }
    
    @Test
    @DisplayName("Lanzar excepción si no hay servicio ni valor cacheado")
    void lanzarExcepcionSiNoHayCacheNiServicio() {
        // Arrange
        OperacionRequest request = new OperacionRequest("10", "10");

        // Simular fallo del servicio externo
        when(porcentajeServiceMock.obtenerPorcentaje()).thenThrow(new RuntimeException("Servicio caído"));

        // Simular caché vacío devolviendo null desde el get()
        Cache mockCache = mock(Cache.class);
        when(cacheManager.getCache("porcentaje")).thenReturn(mockCache);
        when(mockCache.get(SimpleKey.EMPTY, Double.class)).thenReturn(null);

        // Act & Assert
        assertThrows(PorcentajeNoDisponibleException.class,
                () -> operacionService.calcularOperacion(request));
    }
    
    @Test
    @DisplayName("calcularConAuditoria- guardado exitoso")
    void calcularConAuditoriaDebeGuardarHistorialExitoso() throws JsonProcessingException {
        // Arrange
        OperacionRequest request = new OperacionRequest("10", "5");
        double porcentaje = 10.0;
        when(porcentajeServiceMock.obtenerPorcentaje()).thenReturn(porcentaje);
        
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getRequestURI()).thenReturn("/api/v1/operacion/calcular");

        ObjectMapper mapper = new ObjectMapper();
        operacionService = new OperacionServiceImpl(
            porcentajeServiceMock, cacheManager, historialOperacionService, mapper
        );

        // Act
        OperacionResponse response = operacionService.calcularConAuditoria(request, httpRequest);

        // Assert resultado
        double esperado = (15 + (15 * 10.0 / 100));
        assertEquals(esperado, response.getResultado(), 0.001);

        // Capturar los argumentos pasados al método guardar
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> reqJsonCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> resJsonCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> errorCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(historialOperacionService).guardar(
                uriCaptor.capture(),
                reqJsonCaptor.capture(),
                resJsonCaptor.capture(),
                errorCaptor.capture()
            );

        // Verificar valores
        assertEquals("/api/v1/operacion/calcular", uriCaptor.getValue());
        assertFalse(errorCaptor.getValue());

        String reqJson = reqJsonCaptor.getValue();
        assertTrue(reqJson.contains("\"num1\":\"10\""));
        assertTrue(reqJson.contains("\"num2\":\"5\""));

        String resJson = resJsonCaptor.getValue();
        assertTrue(resJson.contains("\"resultado\":16.5"));
        assertTrue(resJson.contains("\"porcentajeAplicado\":10.0"));
    }
    
    @Test
    @DisplayName("calcularConAuditoria - falla de servicio y caché debe guardar historial con error=true")
    void calcularConAuditoriaDebeGuardarHistorialEnFalloTotal() {
        // Arrange
        OperacionRequest request = new OperacionRequest("10", "5");
        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getRequestURI()).thenReturn("/api/v1/operacion/calcular");

        // Simular fallo del servicio externo
        when(porcentajeServiceMock.obtenerPorcentaje()).thenThrow(new RuntimeException("Servicio caído"));

        // Simular fallo del caché
        Cache mockCache = mock(Cache.class);
        when(cacheManager.getCache("porcentaje")).thenReturn(mockCache);
        when(mockCache.get(SimpleKey.EMPTY, Double.class)).thenReturn(null);

        // Act & Assert
        assertThrows(PorcentajeNoDisponibleException.class,
            () -> operacionService.calcularConAuditoria(request, httpRequest));

        // Capturar los argumentos de auditoría
        ArgumentCaptor<String> uriCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> reqCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> resCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> errorCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(historialOperacionService).guardar(
            uriCaptor.capture(),
            reqCaptor.capture(),
            resCaptor.capture(),
            errorCaptor.capture()
        );

        assertEquals("/api/v1/operacion/calcular", uriCaptor.getValue());
        assertTrue(errorCaptor.getValue(), "Debe marcar error=true en caso de excepción");
    }


}
