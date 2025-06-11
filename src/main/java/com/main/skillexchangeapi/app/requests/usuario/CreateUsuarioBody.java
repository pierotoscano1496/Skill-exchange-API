package com.main.skillexchangeapi.app.requests.usuario;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import com.main.skillexchangeapi.app.constants.UsuarioConstants;

@Data
public class CreateUsuarioBody {
    private String dni;
    private String carnetExtranjeria;
    private UsuarioConstants.TipoDocumento tipoDocumento;
    private String correo;
    private String nombres;
    private String apellidos;
    private UsuarioConstants.Tipo tipo;
    private LocalDate fechaNacimiento;
    private String clave;
    private String perfilLinkedin;
    private String perfilFacebook;
    private String perfilInstagram;
    private String perfilTiktok;
    private String introduccion;
    private List<AsignacionSkillToUsuarioRequest> skills;
}
