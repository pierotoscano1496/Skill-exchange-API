package com.main.skillexchangeapi.app.responses.usuario;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.UsuarioConstants.Tipo;
import com.main.skillexchangeapi.app.constants.UsuarioConstants.TipoDocumento;
import com.main.skillexchangeapi.app.responses.skill.SkillAsignadoResponse;

@Data
@Builder
public class UsuarioRegisteredResponse {
    private UUID id;
    private String dni;
    private String carnetExtranjeria;
    private TipoDocumento tipoDocumento;
    private String correo;
    private String nombres;
    private String apellidos;
    private Tipo tipo;
    private LocalDate fechaNacimiento;
    private String perfilLinkedin;
    private String perfilFacebook;
    private String perfilInstagram;
    private String perfilTiktok;
    private String introduccion;
    private List<SkillAsignadoResponse> skills;
}
