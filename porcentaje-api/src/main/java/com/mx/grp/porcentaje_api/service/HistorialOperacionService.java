package com.mx.grp.porcentaje_api.service;

import org.springframework.data.domain.Pageable;

import com.mx.grp.porcentaje_api.dto.response.HistorialPageResponse;

public interface HistorialOperacionService {

	void guardar (String endpoint, String parametros, String resultado, boolean error);
	
	  HistorialPageResponse obtenerHistorial(Pageable pageable);
}
