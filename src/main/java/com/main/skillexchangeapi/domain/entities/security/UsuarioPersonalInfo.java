package com.main.skillexchangeapi.domain.entities.security;

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
