package com.mx.grp.porcentaje_api.exception;


public class PorcentajeNoDisponibleException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PorcentajeNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}