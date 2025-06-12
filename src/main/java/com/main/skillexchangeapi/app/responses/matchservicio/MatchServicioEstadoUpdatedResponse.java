package com.main.skillexchangeapi.app.responses.matchservicio;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;

@Data
@Builder
public class MatchServicioEstadoUpdatedResponse {
    private UUID id;
    private UUID idServicio;
    private UUID idCliente;
    private LocalDate fecha;
    private LocalDate fechaInicio;
    private LocalDate fechaCierre;
    private Estado estado;
    private int puntuacion;
    private double costo;
}
