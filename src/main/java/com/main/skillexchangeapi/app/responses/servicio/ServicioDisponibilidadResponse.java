package com.main.skillexchangeapi.app.responses.servicio;

import java.time.LocalTime;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ServicioConstants.Dia;
import com.main.skillexchangeapi.domain.entities.detail.ServicioDisponibilidad;

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

    public static ServicioDisponibilidadResponse fromEntity(ServicioDisponibilidad disponibilidad) {
        return ServicioDisponibilidadResponse.builder()
                .id(disponibilidad.getId())
                .idServicio(
                        disponibilidad.getServicio().getId())
                .dia(disponibilidad.getDia())
                .horaInicio(disponibilidad.getHoraInicio())
                .horaFin(disponibilidad.getHoraFin())
                .build();
    }
}
