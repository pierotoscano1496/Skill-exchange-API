package com.main.skillexchangeapi.domain.logical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioCredenciales {
    private String email;
    private String clave; // Entrada de clave:
}
