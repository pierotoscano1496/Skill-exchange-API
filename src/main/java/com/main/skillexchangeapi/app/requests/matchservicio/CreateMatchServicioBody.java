package com.main.skillexchangeapi.app.requests.matchservicio;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateMatchServicioBody extends CreateFirstMatchServicioBody {
    private UUID idCliente;
    private int puntuacion;
    private double costo;
}
