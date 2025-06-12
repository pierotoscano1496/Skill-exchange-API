package com.main.skillexchangeapi.app.responses.servicio;

import java.time.LocalTime;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ServicioConstants.Dia;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioDisponibilidadResponse {
    private UUID id;
    private UUID idServicio;
    private Dia dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
