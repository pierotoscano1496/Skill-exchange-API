package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ModalidadPago {
    private final UUID id;
    private String tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private String url;
    private Servicio servicio;
}
