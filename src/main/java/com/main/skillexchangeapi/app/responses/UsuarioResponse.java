package com.main.skillexchangeapi.app.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Builder
public class UsuarioResponse {
    private final UUID id;
    private String dni;
    private String carnetExtranjeria;
    private String tipoDocumento;
    private String tipo;
    private String introduccion;
    private String correo;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String perfilLinkedin;
    private String perfilFacebook;
    private String perfilInstagram;
    private String perfilTiktok;
}
