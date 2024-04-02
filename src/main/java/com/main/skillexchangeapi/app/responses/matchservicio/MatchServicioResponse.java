package com.main.skillexchangeapi.app.responses.matchservicio;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class MatchServicioResponse {
    private UUID id;
    private UUID idServicio;
    private UUID idCliente;
    private LocalDate fecha;
    private LocalDate fechaInicio;
    private LocalDate fechaCierre;
    private String estado;
    private int puntuacion;
    private double costo;
}
