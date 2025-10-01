package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.usuario.AsignacionSkillToUsuarioRequest;
import com.main.skillexchangeapi.app.constants.UsuarioConstants.TipoDocumento;
import com.main.skillexchangeapi.app.requests.SetPlanToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.matchservicio.CreateFirstMatchServicioBody;
import com.main.skillexchangeapi.app.requests.usuario.CreateUsuarioBody;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.app.responses.skill.SkillAsignadoResponse;
import com.main.skillexchangeapi.app.responses.skill.SkillInfoResponse;
import com.main.skillexchangeapi.app.responses.usuario.PlanAsignado;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioRegisteredResponse;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioSkillsAsignadosResponse;
import com.main.skillexchangeapi.app.security.entities.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotDeletedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.logical.UsuarioCredenciales;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IUsuarioService {
        UsuarioResponse obtener(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException;

        UsuarioRegisteredResponse obtener(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException;

        UsuarioResponse login(UsuarioCredenciales credenciales)
                        throws DatabaseNotWorkingException, EncryptionAlghorithmException, ResourceNotFoundException;

        UsuarioPersonalInfo getUserCred(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException;

        UsuarioRegisteredResponse registrar(CreateUsuarioBody requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException;

        // Gestión de skills para usuario autenticado
        UsuarioSkillsAsignadosResponse asignarSkills(UUID id, List<AsignacionSkillToUsuarioRequest> requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException;

        UsuarioSkillsAsignadosResponse asignarSkills(String correo, List<AsignacionSkillToUsuarioRequest> requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException;

        SkillAsignadoResponse asignarSkill(String correo,
                        AsignacionSkillToUsuarioRequest requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException;

        List<SkillResponse> obtenerSkills(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException;

        List<SkillInfoResponse> obtenerSkillsInfo(String correo)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        List<SkillAsignadoResponse> obtenerSkillsAsignados(String correo)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        Boolean checkIfSkillExistsInServicios(UUID idSkill, String correo) throws DatabaseNotWorkingException,
                        ResourceNotFoundException;

        PlanAsignado asignarPlan(UUID id, SetPlanToUsuarioRequest request)
                        throws DatabaseNotWorkingException, NotCreatedException;

        boolean existsBy(TipoDocumento tipoDocumento, String documento, String correo)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        boolean deleteSkil(UUID idSkill, String correo)
                        throws DatabaseNotWorkingException, ResourceNotFoundException, NotDeletedException;

        // Gestión de matchs para usuario autenticado
        MatchServicioResponse registrarMatch(CreateFirstMatchServicioBody requestBody, HttpServletRequest request)
                        throws ResourceNotFoundException, DatabaseNotWorkingException, NotCreatedException;

        // Métodos para ambiente de desarrollo o pruebas
        List<UsuarioResponse> obtenerTodos() throws DatabaseNotWorkingException;

        UsuarioResponse restorePassword(UUID id, Map<String, Object> body)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;
}
