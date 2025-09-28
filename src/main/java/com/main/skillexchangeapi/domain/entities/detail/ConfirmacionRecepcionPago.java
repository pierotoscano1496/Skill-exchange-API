package com.main.skillexchangeapi.domain.entities.detail;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;
import com.main.skillexchangeapi.domain.entities.MatchServicio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmacionRecepcionPago {
    private UUID id;
    private MatchServicio matchServicio;
    private boolean pagoCompletoAcordado;
    private boolean metodoPagoAcordado;
    private boolean comprobanteRecibido;
    private boolean montoRecibidoCorrecto;
    private ModalidadPagoConstants.Tipo metodoPagoRecibido;
    private double montoRecibido;
    private String numeroComprobante;
    private String notasAdicionales;
    private boolean confirmacionEjecucionServicio;
}
