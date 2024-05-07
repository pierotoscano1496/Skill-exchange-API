package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.usuario.AsignacionSkillToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.SetPlanToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.usuario.CreateUsuarioBody;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.UsuarioSkillsResponse;
import com.main.skillexchangeapi.app.responses.usuario.PlanAsignado;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioRegisteredResponse;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioSkillsAsignadosResponse;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.app.security.entities.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.logical.UsuarioCredenciales;

import java.util.List;
import java.util.UUID;

public interface IUsuarioService {
    UsuarioResponse obtener(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException;

    UsuarioRegisteredResponse obtener(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException;

    UsuarioResponse login(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, EncryptionAlghorithmException, ResourceNotFoundException;

    UsuarioPersonalInfo getUserCred(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException;

    UsuarioRegisteredResponse registrar(CreateUsuarioBody requestBody) throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException;

    UsuarioSkillsAsignadosResponse asignarSkills(UUID id, List<AsignacionSkillToUsuarioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException;

    List<UsuarioSkillsResponse> obtenerSkills(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException;

    PlanAsignado asignarPlan(UUID id, SetPlanToUsuarioRequest request) throws DatabaseNotWorkingException, NotCreatedException;
}
