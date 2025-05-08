package com.mx.grp.porcentaje_api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialOperacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime fechaHora;
	
	private String endpoint;
	
	 @Column(columnDefinition = "TEXT")
	private String parametros;
	
	 @Column(columnDefinition = "TEXT")
	private String resultado;
	
	private boolean error;
	
	public boolean  Error() {
		return error;
	}	
	
	public void setError(boolean error) {
	    this.error = error;
	}
}
