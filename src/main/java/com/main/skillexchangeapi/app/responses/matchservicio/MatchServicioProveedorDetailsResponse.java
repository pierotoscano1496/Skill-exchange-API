package com.main.skillexchangeapi.app.responses.matchservicio;

import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class MatchServicioProveedorDetailsResponse {
    private UUID id;
    private ServicioResponse servicio;
    private UsuarioResponse proveedor;
    private LocalDate fecha;
    private LocalDate fechaInicio;
    private LocalDate fechaCierre;
    private String estado;
    private int puntuacion;
    private double costo;
}
