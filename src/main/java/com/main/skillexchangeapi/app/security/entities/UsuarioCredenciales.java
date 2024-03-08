package com.main.skillexchangeapi.app.security.entities;

import lombok.Data;
import lombok.Getter;

@Getter
public class UsuarioCredenciales {
    private String email;
    private String password;
}
