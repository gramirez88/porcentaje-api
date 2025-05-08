package com.mx.grp.porcentaje_api.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialPageResponse {

	private List<HistorialResponse> items;
    private int totalPages;
    private long totalElements;
}
