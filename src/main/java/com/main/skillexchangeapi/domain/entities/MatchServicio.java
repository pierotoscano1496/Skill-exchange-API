package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;

@Data
@Builder
public class MatchServicio {
    private final UUID id;
    private Servicio servicio;
    private Usuario cliente;
    private LocalDateTime fecha;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCierre;
    private Estado estado;
    private int puntuacion;
    private double costo;
    private String mensaje;
}
