package com.mx.grp.porcentaje_api.service.externo_mock;

import java.util.Random;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PorcentajeServiceMockImpl implements PorcentajeServiceMock {

	private final Random random = new Random();

	@Override
	@Cacheable("porcentaje")
	public double obtenerPorcentaje() {
		
		if (random.nextBoolean()) {
			throw new RuntimeException("Servicio externo no disponible");
		}
		//Simulamos un servicio externo que devuelve un 10% fijo.
		return 10.0;
	}

}
