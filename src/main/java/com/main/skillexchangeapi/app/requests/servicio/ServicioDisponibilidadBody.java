package com.main.skillexchangeapi.app.requests.servicio;

import java.time.LocalTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public class ServicioDisponibilidadBody {
    private UUID idServicio;
    private String dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
