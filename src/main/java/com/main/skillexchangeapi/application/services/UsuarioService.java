package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.requests.usuario.AsignacionSkillToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.SetPlanToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.usuario.CreateUsuarioBody;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.UsuarioSkillsResponse;
import com.main.skillexchangeapi.app.responses.usuario.PlanAsignado;
import com.main.skillexchangeapi.app.responses.usuario.SkillAsignado;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioRegisteredResponse;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioSkillsAsignadosResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISkillUsuarioRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.app.security.entities.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.logical.UsuarioCredenciales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private IUsuarioRepository repository;

    @Autowired
    private ISkillUsuarioRepository skillUsuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponse obtener(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        Usuario usuario = repository.obtenerById(id);
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .dni(usuario.getDni())
                .carnetExtranjeria(usuario.getCarnetExtranjeria())
                .tipoDocumento(usuario.getTipoDocumento())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .correo(usuario.getCorreo())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .introduccion(usuario.getIntroduccion())
                .perfilFacebook(usuario.getPerfilFacebook())
                .perfilInstagram(usuario.getPerfilInstagram())
                .perfilLinkedin(usuario.getPerfilLinkedin())
                .perfilTiktok(usuario.getPerfilTiktok())
                .build();
    }

    @Override
    public UsuarioRegisteredResponse obtener(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException {
        Usuario usuario = repository.obtenerByCorreo(correo);
        return UsuarioRegisteredResponse.builder()
                .id(usuario.getId())
                .dni(usuario.getDni())
                .carnetExtranjeria(usuario.getCarnetExtranjeria())
                .tipoDocumento(usuario.getTipoDocumento())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .correo(usuario.getCorreo())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .introduccion(usuario.getIntroduccion())
                .perfilFacebook(usuario.getPerfilFacebook())
                .perfilInstagram(usuario.getPerfilInstagram())
                .perfilLinkedin(usuario.getPerfilLinkedin())
                .perfilTiktok(usuario.getPerfilTiktok())
                .build();
    }

    @Override
    public UsuarioResponse login(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, EncryptionAlghorithmException, ResourceNotFoundException {
        // Cifrar clave de usuario
        credenciales.setClave(passwordEncoder.encode(credenciales.getClave()));

        Usuario usuario = repository.login(credenciales);

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .dni(usuario.getDni())
                .carnetExtranjeria(usuario.getCarnetExtranjeria())
                .tipoDocumento(usuario.getTipoDocumento())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .fechaNacimiento(usuario.getFechaNacimiento())
                .correo(usuario.getCorreo())
                .perfilLinkedin(usuario.getPerfilLinkedin())
                .perfilFacebook(usuario.getPerfilFacebook())
                .perfilInstagram(usuario.getPerfilInstagram())
                .perfilTiktok(usuario.getPerfilTiktok())
                .build();
    }

    @Override
    public UsuarioPersonalInfo getUserCred(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.getUserCred(correo);
    }

    @Override
    public UsuarioRegisteredResponse registrar(CreateUsuarioBody requestBody) throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException {
        // Cifrar clave de usuario
        // Validar existencia de usuario previa antes de registrar
        Usuario usuarioExistente = null;
        switch (requestBody.getTipoDocumento()) {
            case "dni":
                usuarioExistente = repository.validarExistenciaByDni(requestBody.getDni(), requestBody.getCorreo());
                break;
            case "carnet-extranjeria":
                usuarioExistente = repository.validarExistenciaByCarnetExtranjeria(requestBody.getCarnetExtranjeria(), requestBody.getCorreo());
                break;
            default:
                throw new NotCreatedException("El usuario no tiene una identificación válida");
        }

        if (usuarioExistente == null) {
            String claveCifrada = passwordEncoder.encode(requestBody.getClave());
            requestBody.setClave(claveCifrada);

            Usuario usuarioRegistered = repository.registrar(Usuario.builder()
                    .dni(requestBody.getDni())
                    .carnetExtranjeria(requestBody.getCarnetExtranjeria())
                    .tipoDocumento(requestBody.getTipoDocumento())
                    .correo(requestBody.getCorreo())
                    .nombres(requestBody.getNombres())
                    .apellidos(requestBody.getApellidos())
                    .fechaNacimiento(requestBody.getFechaNacimiento())
                    .clave(requestBody.getClave())
                    .perfilLinkedin(requestBody.getPerfilLinkedin())
                    .perfilFacebook(requestBody.getPerfilFacebook())
                    .perfilInstagram(requestBody.getPerfilInstagram())
                    .perfilTiktok(requestBody.getPerfilTiktok())
                    .introduccion(requestBody.getIntroduccion())
                    .build());

            return UsuarioRegisteredResponse.builder()
                    .id(usuarioRegistered.getId())
                    .dni(usuarioRegistered.getDni())
                    .carnetExtranjeria(usuarioRegistered.getCarnetExtranjeria())
                    .tipoDocumento(usuarioRegistered.getTipoDocumento())
                    .correo(usuarioRegistered.getCorreo())
                    .nombres(usuarioRegistered.getNombres())
                    .apellidos(usuarioRegistered.getApellidos())
                    .fechaNacimiento(usuarioRegistered.getFechaNacimiento())
                    .perfilLinkedin(usuarioRegistered.getPerfilLinkedin())
                    .perfilFacebook(usuarioRegistered.getPerfilFacebook())
                    .perfilInstagram(usuarioRegistered.getPerfilInstagram())
                    .perfilTiktok(usuarioRegistered.getPerfilTiktok())
                    .introduccion(usuarioRegistered.getIntroduccion())
                    .build();
        } else {
            throw new NotCreatedException("El usuario ya existe");
        }
    }

    @Override
    public UsuarioSkillsAsignadosResponse asignarSkills(UUID id, List<AsignacionSkillToUsuarioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        List<SkillUsuario> skillsUsuario = requestBody.stream().map(s -> SkillUsuario.builder()
                .skill(Skill.builder()
                        .id(s.getIdSkill())
                        .build())
                .usuario(Usuario.builder()
                        .id(id)
                        .build())
                .nivelConocimiento(s.getNivelConocimiento())
                .descripcion(s.getDescripcion())
                .build()).collect(Collectors.toList());

        List<SkillUsuario> skillsUsuarioRegistered = skillUsuarioRepository.registrarMultiple(skillsUsuario);

        List<SkillAsignado> skillAsignadosRegistered = skillsUsuarioRegistered.stream().map(su ->
                        SkillAsignado.builder()
                                .id(su.getSkill().getId())
                                .nivelConocimiento(su.getNivelConocimiento())
                                .descripcion(su.getDescripcion())
                                .build())
                .toList();

        return UsuarioSkillsAsignadosResponse.builder()
                .id(id)
                .skillsAsignados(skillAsignadosRegistered)
                .build();
    }

    @Override
    public List<SkillResponse> obtenerSkills(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return skillUsuarioRepository.obtenerByIdUsuario(id)
                .stream().map(s -> SkillResponse.builder()
                        .id(s.getSkill().getId())
                        .descripcion(s.getSkill().getDescripcion())
                        .idSubCategoria(s.getSkill().getSubCategoria().getId())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public PlanAsignado asignarPlan(UUID id, SetPlanToUsuarioRequest requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        PlanUsuario planUsuarioAsignado = repository.asignarPlan(PlanUsuario.builder()
                .plan(Plan.builder()
                        .id(requestBody.getIdPlan())
                        .build())
                .usuario(Usuario.builder()
                        .id(id)
                        .build())
                .isActive(requestBody.isActive())
                .monto(requestBody.getMonto())
                .moneda(requestBody.getMoneda())
                .build());

        return PlanAsignado.builder()
                .idPlan(planUsuarioAsignado.getPlan().getId())
                .idUsuario(planUsuarioAsignado.getUsuario().getId())
                .isActive(planUsuarioAsignado.isActive())
                .monto(planUsuarioAsignado.getMonto())
                .moneda(planUsuarioAsignado.getMoneda())
                .build();
    }
}
