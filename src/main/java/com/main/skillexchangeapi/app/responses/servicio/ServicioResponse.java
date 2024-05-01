package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ServicioResponse {
    private UUID id;
    private UUID idSkill;
    private UUID idUsuario;
    private double precio;
    private String titulo;
    private String descripcion;
}
