package com.main.skillexchangeapi.app.responses.servicio;

import java.time.LocalTime;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ServicioConstants.Dia;
import com.main.skillexchangeapi.domain.entities.Servicio;
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
        if (disponibilidad == null) {
            return null;
        }

        Servicio servicio = disponibilidad.getServicio();

        return ServicioDisponibilidadResponse.builder()
                .id(disponibilidad.getId())
                .idServicio(servicio != null ? servicio.getId() : null)
                .dia(disponibilidad.getDia())
                .horaInicio(disponibilidad.getHoraInicio())
                .horaFin(disponibilidad.getHoraFin())
                .build();
    }
}
