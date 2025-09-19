package com.main.skillexchangeapi.app.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.UsuarioConstants.TipoDocumento;
import com.main.skillexchangeapi.app.responses.skill.SkillAsignadoResponse;
import com.main.skillexchangeapi.app.responses.skill.SkillInfoResponse;
import com.main.skillexchangeapi.domain.entities.Usuario;

@Setter
@Getter
@Builder
public class UsuarioResponse {
    private final UUID id;
    private String dni;
    private String carnetExtranjeria;
    private TipoDocumento tipoDocumento;
    private String introduccion;
    private String correo;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String perfilLinkedin;
    private String perfilFacebook;
    private String perfilInstagram;
    private String perfilTiktok;
    private List<SkillAsignadoResponse> skillsAsignados;

    public static UsuarioResponse fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        List<SkillAsignadoResponse> skillsAsignados = null;

        if (usuario.getSkillsDetails() != null && !usuario.getSkillsDetails().isEmpty()) {
            skillsAsignados = usuario.getSkillsDetails().stream()
                    .map(SkillAsignadoResponse::fromEntity)
                    .toList();
        }

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .dni(usuario.getDni())
                .carnetExtranjeria(usuario.getCarnetExtranjeria())
                .tipoDocumento(usuario.getTipoDocumento())
                .introduccion(usuario.getIntroduccion())
                .correo(usuario.getCorreo())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .perfilLinkedin(usuario.getPerfilLinkedin())
                .perfilFacebook(usuario.getPerfilFacebook())
                .perfilInstagram(usuario.getPerfilInstagram())
                .perfilTiktok(usuario.getPerfilTiktok())
                .skillsAsignados(skillsAsignados)
                .build();
    }
}
