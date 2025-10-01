package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.apirest.controllers.UsuarioController;
import com.main.skillexchangeapi.app.constants.UsuarioConstants.TipoDocumento;
import com.main.skillexchangeapi.app.security.entities.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.logical.UsuarioCredenciales;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IUsuarioRepository {
        Usuario login(UsuarioCredenciales credenciales)
                        throws DatabaseNotWorkingException, ResourceNotFoundException, EncryptionAlghorithmException;

        Usuario obtenerById(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException;

        Usuario obtenerByCorreo(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException;

        Usuario validarExistenciaByDni(String dni, String correo) throws DatabaseNotWorkingException;

        Usuario validarExistenciaByCarnetExtranjeria(String carnetExtranjeria, String correo)
                        throws DatabaseNotWorkingException;

        UsuarioPersonalInfo getUserCred(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException;

        Usuario registrar(Usuario usuario)
                        throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException;

        PlanUsuario asignarPlan(PlanUsuario planUsuario) throws DatabaseNotWorkingException, NotCreatedException;

        int deleteAll() throws DatabaseNotWorkingException;

        boolean existsByParams(Map<String, String> params)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;

        // Métodos para ambiente de desarrollo o pruebas
        List<Usuario> obtenerTodos() throws DatabaseNotWorkingException;

        Usuario updatePassword(UUID id, String password)
                        throws DatabaseNotWorkingException, ResourceNotFoundException;
}
