package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.security.UserDetailsImpl;
import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.entities.security.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        try {
            UsuarioPersonalInfo personalInfo = usuarioRepository.getUserCred(correo);
            return new UserDetailsImpl(personalInfo);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
