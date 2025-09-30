package com.main.skillexchangeapi.app.requests.matchservicio;

import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;

import lombok.Getter;

@Getter
public class ConfirmacionRecepcionPagoBody {
    private UUID id;
    private UUID idMatchServicio;
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
