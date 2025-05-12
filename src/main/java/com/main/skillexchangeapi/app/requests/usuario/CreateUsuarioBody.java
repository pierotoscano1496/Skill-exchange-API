package com.main.skillexchangeapi.app.requests.usuario;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateUsuarioBody {
    private String dni;
    private String carnetExtranjeria;
    private String tipoDocumento;
    private String correo;
    private String nombres;
    private String apellidos;
    private String tipo;
    private LocalDate fechaNacimiento;
    private String clave;
    private String perfilLinkedin;
    private String perfilFacebook;
    private String perfilInstagram;
    private String perfilTiktok;
    private String introduccion;
    private List<AsignacionSkillToUsuarioRequest> skills;
}
