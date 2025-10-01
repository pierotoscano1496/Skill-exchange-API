package com.main.skillexchangeapi.domain.entities;

import com.main.skillexchangeapi.app.constants.UsuarioConstants.TipoDocumento;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Usuario {
    private final UUID id;
    private String dni;
    private String carnetExtranjeria;
    private TipoDocumento tipoDocumento;
    private String correo;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String clave;
    private String perfilLinkedin;
    private String perfilFacebook;
    private String perfilInstagram;
    private String perfilTiktok;
    private String introduccion;
    private List<SkillUsuario> skillsDetails;
    private List<PlanUsuario> planesDetails;
}
