package com.main.skillexchangeapi.app.responses.matchservicio;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.domain.entities.MatchServicio;

@Data
@Builder
public class MatchServicioResponse {
    private UUID id;
    private UUID idServicio;
    private UUID idCliente;
    private LocalDateTime fecha;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCierre;
    private Estado estado;
    private int puntuacion;
    private double costo;

    public static MatchServicioResponse fromEntity(MatchServicio matchServicio) {
        return MatchServicioResponse.builder()
                .id(matchServicio.getId())
                .idServicio(matchServicio.getServicio().getId())
                .idCliente(matchServicio.getCliente().getId())
                .costo(matchServicio.getCosto())
                .estado(matchServicio.getEstado())
                .puntuacion(matchServicio.getPuntuacion())
                .fecha(matchServicio.getFecha())
                .fechaInicio(matchServicio.getFechaInicio())
                .fechaCierre(matchServicio.getFechaCierre())
                .build();
    }
}
