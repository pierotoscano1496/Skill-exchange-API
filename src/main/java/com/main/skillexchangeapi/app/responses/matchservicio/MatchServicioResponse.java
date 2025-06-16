package com.main.skillexchangeapi.app.responses.matchservicio;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;

@Data
@Builder
public class MatchServicioResponse {
    private UUID id;
    private UUID idServicio;
    private UUID idCliente;
    private LocalDateTime fecha;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCierre;
    private Estado estado;
    private int puntuacion;
    private double costo;
}
