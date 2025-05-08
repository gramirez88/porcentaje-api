package com.mx.grp.porcentaje_api.dto.response;

public class OperacionResponse {

	private double resultado;
	private double porcentajeAplicado;
	
	public OperacionResponse() {
	
	}
	
	public OperacionResponse(double resultado, double porcentajeAplicado) {
		super();
		this.resultado = resultado;
		this.porcentajeAplicado = porcentajeAplicado;
	}
	
	
	public double getResultado() {
		return resultado;
	}
	public void setResultado(double resultado) {
		this.resultado = resultado;
	}
	public double getPorcentajeAplicado() {
		return porcentajeAplicado;
	}
	public void setPorcentajeAplicado(double porcentajeAplicado) {
		this.porcentajeAplicado = porcentajeAplicado;
	}
	

	
}
