package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ServicioBusquedaResponse {
    private UUID id;
    private String titulo;
    private String descripcion;
    private double precio;
    private UUID idUsuario;
    private String nombresUsuario;
    private String apellidosUsuario;
    private String correoUsuario;
    private UUID idSkill;
    private String descripcionSkill;
}
