package com.main.skillexchangeapi.domain.entities;

import lombok.Data;

@Data
public class ModalidadPago {
    private final Long id;
    private String nombre;
    private String descripcion;
}
