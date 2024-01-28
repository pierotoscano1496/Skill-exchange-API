package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.requests.AsignacionSkillToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.SetPlanToUsuarioRequest;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.UsuarioSkillsResponse;
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
    public UsuarioResponse login(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, EncryptionAlghorithmException, ResourceNotFoundException {
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
    public Usuario registrar(Usuario usuario) throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException {
        // Cifrar clave de usuario
        String claveCifrada = passwordEncoder.encode(usuario.getClave());
        usuario.setClave(claveCifrada);

        return repository.registrar(usuario);
    }

    @Override
    public List<UsuarioSkillsResponse> asignarSkills(UUID id, List<AsignacionSkillToUsuarioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        Usuario usuario = Usuario.builder()
                .id(id)
                .build();

        List<SkillUsuario> skillsUsuario = requestBody.stream().map(s -> SkillUsuario.builder()
                .skill(Skill.builder()
                        .id(s.getIdSkill())
                        .build())
                .usuario(usuario)
                .nivelConocimiento(s.getNivelConocimiento())
                .descripcion(s.getDescripcion())
                .build()).collect(Collectors.toList());

        return skillUsuarioRepository.registrarMultiple(skillsUsuario)
                .stream().map(s -> UsuarioSkillsResponse.builder()
                        .idSkill(s.getSkill().getId())
                        .nombreCategoria(s.getSkill().getSubCategoria().getCategoria().getNombre())
                        .nombreSubCategoria(s.getSkill().getSubCategoria().getNombre())
                        .nivelConocimiento(s.getNivelConocimiento())
                        .descripcionSkill(s.getDescripcion())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public List<UsuarioSkillsResponse> obtenerSkills(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return skillUsuarioRepository.obtenerByIdUsuario(id)
                .stream().map(s -> UsuarioSkillsResponse.builder()
                        .idSkill(s.getSkill().getId())
                        .descripcionSkill(s.getSkill().getDescripcion())
                        .nombreSubCategoria(s.getSkill().getSubCategoria().getNombre())
                        .nombreCategoria(s.getSkill().getSubCategoria().getCategoria().getNombre())
                        .nivelConocimiento(s.getNivelConocimiento())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public PlanUsuario asignarPlan(UUID id, SetPlanToUsuarioRequest requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        Plan plan = Plan.builder()
                .id(requestBody.getIdPlan())
                .build();

        Usuario usuario = Usuario.builder()
                .id(id)
                .build();

        PlanUsuario planUsuario = PlanUsuario.builder()
                .plan(plan)
                .usuario(usuario)
                .isActive(requestBody.isActive())
                .monto(requestBody.getMonto())
                .moneda(requestBody.getMoneda())
                .build();

        return repository.asignarPlan(planUsuario);
    }
}
