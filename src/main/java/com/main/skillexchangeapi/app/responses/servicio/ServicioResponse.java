package com.main.skillexchangeapi.app.responses.servicio;

import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
public class ServicioResponse {
    private UUID id;
    private UUID idSkill;
    private double precio;
    private String titulo;
    private String descripcion;
    private String urlImagePreview;
    private UsuarioResponse usuario;
}
