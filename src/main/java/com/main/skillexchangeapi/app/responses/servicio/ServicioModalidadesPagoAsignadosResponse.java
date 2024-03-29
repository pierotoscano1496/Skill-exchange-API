package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ServicioModalidadesPagoAsignadosResponse {
    private UUID id;
    private List<ModalidadPagoAsignado> modalidadesPagoAsignado;
}
