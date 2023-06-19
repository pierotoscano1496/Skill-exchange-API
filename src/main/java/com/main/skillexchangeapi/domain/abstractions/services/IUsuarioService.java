package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.entities.security.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.logical.UsuarioContentRegister;
import com.main.skillexchangeapi.domain.logical.UsuarioCredenciales;

import java.util.ArrayList;

public interface IUsuarioService {
    public Usuario login(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, EncryptionAlghorithmException, ResourceNotFoundException;

    public UsuarioPersonalInfo getUserCred(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException;

    public Usuario registrar(Usuario usuario) throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException;

    public ArrayList<SkillUsuario> asignarSkills(Long id, ArrayList<SkillUsuario> skillsUsuario) throws DatabaseNotWorkingException, NotCreatedException;

    public ArrayList<SkillUsuario> obtenerSkills(Long id) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
