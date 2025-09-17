package com.main.skillexchangeapi.app.responses.servicio;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants.Tipo;
import com.main.skillexchangeapi.domain.entities.ModalidadPago;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModalidadPagoResponse {
    private UUID id;
    private Tipo tipo;
    private String cuentaBancaria;
    private String numeroCelular;
    private String url;

    public static ModalidadPagoResponse fromEntity(ModalidadPago modalidadPago) {
        return ModalidadPagoResponse.builder()
                .id(modalidadPago.getId())
                .tipo(modalidadPago.getTipo())
                .cuentaBancaria(modalidadPago.getCuentaBancaria())
                .numeroCelular(modalidadPago.getNumeroCelular())
                .url(modalidadPago.getUrl())
                .build();
    }
}
