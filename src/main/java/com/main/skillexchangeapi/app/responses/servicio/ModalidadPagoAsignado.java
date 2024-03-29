package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ModalidadPagoAsignado {
    private UUID id;
    private String tipo;
    private String cuentaBancaria;
    private String numeroCelular;
}
