package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;

@Data
@Builder
public class MatchServicio {
    private final UUID id;
    private Servicio servicio;
    private Usuario cliente;
    private LocalDate fecha;
    private LocalDate fechaInicio;
    private LocalDate fechaCierre;
    private Estado estado;
    private int puntuacion;
    private double costo;
}
