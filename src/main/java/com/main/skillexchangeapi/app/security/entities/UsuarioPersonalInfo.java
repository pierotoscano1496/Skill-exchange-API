package com.main.skillexchangeapi.app.security.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioPersonalInfo {
    private String nombres;
    private String apellidos;
    private String correo;
    private String clave;
}
