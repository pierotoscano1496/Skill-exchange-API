package com.main.skillexchangeapi.app.responses.matchservicio;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;
import com.main.skillexchangeapi.domain.entities.detail.ConfirmacionRecepcionPago;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmacionRecepcionPagoResponse {
    private UUID id;
    private MatchServicioResponse matchServicio;
    private boolean pagoCompletoAcordado;
    private boolean metodoPagoAcordado;
    private boolean comprobanteRecibido;
    private boolean montoRecibidoCorrecto;
    private ModalidadPagoConstants.Tipo metodoPagoRecibido;
    private double montoRecibido;
    private String numeroComprobante;
    private String notasAdicionales;
    private boolean confirmacionEjecucionServicio;

    public static ConfirmacionRecepcionPagoResponse fromEntity(ConfirmacionRecepcionPago entity) {
        if (entity == null) {
            return null;
        }

        return ConfirmacionRecepcionPagoResponse.builder()
                .id(entity.getId())
                .matchServicio(MatchServicioResponse.fromEntity(entity.getMatchServicio()))
                .pagoCompletoAcordado(entity.isPagoCompletoAcordado())
                .metodoPagoAcordado(entity.isMetodoPagoAcordado())
                .comprobanteRecibido(entity.isComprobanteRecibido())
                .montoRecibidoCorrecto(entity.isMontoRecibidoCorrecto())
                .metodoPagoRecibido(entity.getMetodoPagoRecibido())
                .montoRecibido(entity.getMontoRecibido())
                .numeroComprobante(entity.getNumeroComprobante())
                .notasAdicionales(entity.getNotasAdicionales())
                .confirmacionEjecucionServicio(entity.isConfirmacionEjecucionServicio())
                .build();
    }
}
