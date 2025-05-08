package com.mx.grp.porcentaje_api.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mx.grp.porcentaje_api.dto.response.HistorialPageResponse;
import com.mx.grp.porcentaje_api.dto.response.HistorialResponse;
import com.mx.grp.porcentaje_api.entity.HistorialOperacion;
import com.mx.grp.porcentaje_api.repository.HistorialOperacionRepository;

@Service
public class HistorialOperacionServiceImpl implements HistorialOperacionService {

	private final HistorialOperacionRepository historialRepository;

	public HistorialOperacionServiceImpl(HistorialOperacionRepository historialRepository) {
		this.historialRepository = historialRepository;
	}

	@Async
	@Override
	public void guardar(String endpoint, String parametros, String resultado, boolean error) {
		HistorialOperacion historial = HistorialOperacion.builder()
                .fechaHora(LocalDateTime.now())
                .endpoint(endpoint)
                .parametros(parametros)
                .resultado(resultado)
                .error(error)
                .build();
		historialRepository.save(historial);

	}
	
	  @Override
	    public HistorialPageResponse obtenerHistorial(Pageable pageable) {
		  Page<HistorialResponse> page = historialRepository.findAll(pageable)
				  .map(this::mapToDto);
		  HistorialPageResponse response = new HistorialPageResponse();
		  response.setItems(page.getContent());
		  response.setTotalPages(page.getTotalPages());
		  response.setTotalElements(page.getTotalElements());
		  return response;
	    }

	  private HistorialResponse mapToDto(HistorialOperacion entidad) {
	        HistorialResponse dto = new HistorialResponse();
	        dto.setFechaHora(entidad.getFechaHora());
	        dto.setEndpoint(entidad.getEndpoint());
	        dto.setParametros(entidad.getParametros());
	        dto.setResultado(entidad.getResultado());
	        dto.setError(entidad.Error());
	        return dto;
	    }

}
