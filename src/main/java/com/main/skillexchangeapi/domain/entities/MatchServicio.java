package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class MatchServicio {
    private final UUID id;
    private Servicio servicio;
    private Usuario cliente;
    private Date fecha;
    private Date fechaInicio;
    private Date fechaCierre;
    private int estado;
    private int puntuacion;
    private double costo;
}
