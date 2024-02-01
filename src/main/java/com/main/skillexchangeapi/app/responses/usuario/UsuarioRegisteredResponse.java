package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class UsuarioRegisteredResponse {
    private UUID id;
    private String dni;
    private String carnetExtranjeria;
    private String tipoDocumento;
    private String correo;
    private String nombres;
    private String apellidos;
    private String tipo;
    private LocalDate fechaNacimiento;
    private String perfilLinkedin;
    private String perfilFacebook;
    private String perfilInstagram;
    private String perfilTiktok;
    private String introduccion;
}
