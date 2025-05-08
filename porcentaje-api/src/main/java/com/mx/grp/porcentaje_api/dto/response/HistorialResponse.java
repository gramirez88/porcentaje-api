package com.mx.grp.porcentaje_api.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistorialResponse {

	private LocalDateTime fechaHora;
    private String endpoint;
    private String parametros;
    private String resultado;
    private boolean error;
}
