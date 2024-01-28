package com.main.skillexchangeapi.domain.entities;

import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
public class Usuario {
    private final UUID id;
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
    private ArrayList<SkillUsuario> skillsDetails;
    private ArrayList<PlanUsuario> planesDetails;
}
