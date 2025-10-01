package com.main.skillexchangeapi.app.security.services;

import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.app.security.entities.UsuarioCredenciales;
import com.main.skillexchangeapi.app.security.entities.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.abstractions.security.services.IAuthService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.exceptions.UnsuccessLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public String getTokenLogin(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, ResourceNotFoundException, UnsuccessLoginException {
        UsuarioPersonalInfo info = null;
        info = usuarioRepository.getUserCred(credenciales.getEmail());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(credenciales.getPassword(), info.getClave())) {
            return tokenUtils.createToken(info.getNombres(), info.getCorreo());
        } else {
            throw new UnsuccessLoginException("Error al autenticar el usuario.");
        }
    }
}
