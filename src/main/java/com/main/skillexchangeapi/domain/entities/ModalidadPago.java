package com.main.skillexchangeapi.domain.entities;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants.Tipo;

@Data
@Builder
public class ModalidadPago {
    private final UUID id;
    private Tipo tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private String url;
    private Servicio servicio;
}
