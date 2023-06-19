package com.main.skillexchangeapi.domain.entities;

import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class Usuario {
    private final Long id;
    private String dni;
    private String carnetExtranjeria;
    private String tipoDocumento;
    private String correo;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String clave;
    private String perfilLinkedin;
    private String perfilFacebook;
    private String perfilInstagram;
    private String perfilTiktok;
    private ArrayList<SkillUsuario> skillsDetails;
    private ArrayList<PlanUsuario> planesDetails;
}
