package com.main.skillexchangeapi.app.responses.servicio;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModalidadPagoResponse {
    private UUID id;
    private String tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private String url;

}
