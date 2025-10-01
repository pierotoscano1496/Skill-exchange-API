package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Getter
public class ServicioBusquedaResponse extends ServicioResponse {
    private String nombresUsuario;
    private String apellidosUsuario;
    private String correoUsuario;
    private String descripcionSkill;
}
