package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Match {
    private final UUID id;
    private Servicio servicio;
    private Usuario cliente;
    private LocalDate fecha;
    private LocalDate fechaInicio;
    private LocalDate fechaCierre;
    private int estado;
    private int puntuacion;
    private double costo;
}
