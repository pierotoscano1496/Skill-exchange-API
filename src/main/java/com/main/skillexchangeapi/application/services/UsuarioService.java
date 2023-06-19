package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.entities.security.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.logical.UsuarioContentRegister;
import com.main.skillexchangeapi.domain.logical.UsuarioCredenciales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private IUsuarioRepository repository;

    @Override
    public Usuario login(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, EncryptionAlghorithmException, ResourceNotFoundException {
        return repository.login(credenciales);
    }

    @Override
    public UsuarioPersonalInfo getUserCred(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.getUserCred(correo);
    }

    @Override
    public Usuario registrar(Usuario usuario) throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException {
        return repository.registrar(usuario);
    }

    @Override
    public ArrayList<SkillUsuario> asignarSkills(Long id, ArrayList<SkillUsuario> skillsUsuario) throws DatabaseNotWorkingException, NotCreatedException {
        return repository.asignarSkills(id, skillsUsuario);
    }

    @Override
    public ArrayList<SkillUsuario> obtenerSkills(Long id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtenerSkills(id);
    }
}
