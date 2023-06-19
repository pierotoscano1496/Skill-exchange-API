package com.main.skillexchangeapi.app.security;

import com.main.skillexchangeapi.domain.entities.security.UsuarioPersonalInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private final UsuarioPersonalInfo usuarioPersonalInfo;

    public UserDetailsImpl(UsuarioPersonalInfo usuarioPersonalInfo) {
        this.usuarioPersonalInfo = usuarioPersonalInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return usuarioPersonalInfo.getClave();
    }

    @Override
    public String getUsername() {
        return usuarioPersonalInfo.getCorreo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getNombre() {
        return usuarioPersonalInfo.getNombres();
    }
}
