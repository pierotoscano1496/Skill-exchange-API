package com.main.skillexchangeapi.app.responses.servicio;

import java.time.LocalTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioDisponibilidadResponse {
    private UUID id;
    private UUID idServicio;
    private String dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
