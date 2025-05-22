package com.main.skillexchangeapi.domain.entities.detail;

import java.time.LocalTime;
import java.util.UUID;

import com.main.skillexchangeapi.domain.entities.Servicio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioDisponibilidad {
    private UUID id;
    private Servicio servicio;
    private String dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
