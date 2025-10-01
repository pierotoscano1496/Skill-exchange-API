package com.main.skillexchangeapi.app.responses.matchservicio;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Usuario;

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
        if (matchServicio == null) {
            return null;
        }

        Servicio servicio = matchServicio.getServicio();
        Usuario cliente = matchServicio.getCliente();

        return MatchServicioResponse.builder()
                .id(matchServicio.getId())
                .idServicio(servicio != null ? servicio.getId() : null)
                .idCliente(
                        cliente != null ? cliente.getId() : null)
                .costo(matchServicio.getCosto())
                .estado(matchServicio.getEstado())
                .puntuacion(matchServicio.getPuntuacion())
                .fecha(matchServicio.getFecha())
                .fechaInicio(matchServicio.getFechaInicio())
                .fechaCierre(matchServicio.getFechaCierre())
                .build();
    }
}
