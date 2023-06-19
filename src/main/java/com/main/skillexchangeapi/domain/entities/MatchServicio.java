package com.main.skillexchangeapi.domain.entities;

import lombok.Data;

import java.util.Date;

@Data
public class MatchServicio {
    private final Long id;
    private Servicio servicio;
    private Usuario cliente;
    private Date fecha;
    private Date fechaInicio;
    private Date fechaCierre;
    private int estado;
    private int puntuacion;
    private double costo;
}
