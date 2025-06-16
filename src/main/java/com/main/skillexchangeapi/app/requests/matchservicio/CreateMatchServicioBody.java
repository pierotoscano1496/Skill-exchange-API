package com.main.skillexchangeapi.app.requests.matchservicio;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateMatchServicioBody {
    private UUID idServicio;
    private UUID idCliente;
    private int puntuacion;
    private double costo;
    private String mensaje;
}
