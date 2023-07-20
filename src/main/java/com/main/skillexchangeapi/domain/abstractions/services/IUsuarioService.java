package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.app.requests.SetPlanToUsuarioRequest;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.entities.security.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.logical.UsuarioCredenciales;

import java.util.ArrayList;
import java.util.UUID;

public interface IUsuarioService {
    Usuario login(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, EncryptionAlghorithmException, ResourceNotFoundException;

    UsuarioPersonalInfo getUserCred(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException;

    Usuario registrar(Usuario usuario) throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException;

    ArrayList<SkillUsuario> asignarSkills(UUID id, ArrayList<SkillUsuario> skillsUsuario) throws DatabaseNotWorkingException, NotCreatedException;

    ArrayList<SkillUsuario> obtenerSkills(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException;

    PlanUsuario asignarPlan(UUID id, SetPlanToUsuarioRequest request) throws DatabaseNotWorkingException, NotCreatedException;
}
