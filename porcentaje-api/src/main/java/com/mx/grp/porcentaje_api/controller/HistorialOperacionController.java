package com.mx.grp.porcentaje_api.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mx.grp.porcentaje_api.dto.response.HistorialPageResponse;
import com.mx.grp.porcentaje_api.service.HistorialOperacionService;

@RestController
@RequestMapping("/api/v1/historial")
public class HistorialOperacionController {

	private final HistorialOperacionService historialService;
	
	  public HistorialOperacionController(HistorialOperacionService historialService) {
	        this.historialService = historialService;
	    }
	  
	  @GetMapping
	    public ResponseEntity<HistorialPageResponse> obtenerHistorial(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size
	    ) {
	        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaHora").descending());
	        return ResponseEntity.ok(historialService.obtenerHistorial(pageable));
	    }
}
