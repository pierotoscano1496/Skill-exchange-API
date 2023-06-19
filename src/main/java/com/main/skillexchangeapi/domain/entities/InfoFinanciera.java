package com.main.skillexchangeapi.domain.entities;

import lombok.Data;

@Data
public class InfoFinanciera {
    private final Long id;
    private Usuario usuario;
    private String clave;
}
