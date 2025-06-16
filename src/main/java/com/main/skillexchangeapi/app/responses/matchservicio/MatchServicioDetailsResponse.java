package com.main.skillexchangeapi.app.responses.matchservicio;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MatchServicioDetailsResponse {
    private UUID id;
    private ServicioResponse servicio;
    private UsuarioResponse cliente;
    private LocalDateTime fecha;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCierre;
    private Estado estado;
    private int puntuacion;
    private double costo;
    private String mensaje;
}
