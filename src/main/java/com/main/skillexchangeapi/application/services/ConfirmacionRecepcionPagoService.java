package com.main.skillexchangeapi.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.main.skillexchangeapi.app.requests.matchservicio.ConfirmacionRecepcionPagoBody;
import com.main.skillexchangeapi.app.responses.matchservicio.ConfirmacionRecepcionPagoResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.IConfirmacionRecepcionPagoRepository;
import com.main.skillexchangeapi.domain.abstractions.services.matchactions.IConfirmacionRecepcionPagoService;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.detail.ConfirmacionRecepcionPago;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

@Service
public class ConfirmacionRecepcionPagoService implements IConfirmacionRecepcionPagoService {
    @Autowired
    private IConfirmacionRecepcionPagoRepository repository;

    @Override
    public ConfirmacionRecepcionPagoResponse registrar(ConfirmacionRecepcionPagoBody requestBody)
            throws DatabaseNotWorkingException, NotCreatedException {
        ConfirmacionRecepcionPago confirmacionRegistered = repository.registrar(ConfirmacionRecepcionPago.builder()
                .matchServicio(MatchServicio.builder()
                        .id(requestBody.getIdMatchServicio())
                        .build())
                .pagoCompletoAcordado(requestBody.isPagoCompletoAcordado())
                .metodoPagoAcordado(requestBody.isMetodoPagoAcordado())
                .comprobanteRecibido(requestBody.isComprobanteRecibido())
                .montoRecibidoCorrecto(requestBody.isMontoRecibidoCorrecto())
                .metodoPagoRecibido(requestBody.getMetodoPagoRecibido())
                .montoRecibido(requestBody.getMontoRecibido())
                .numeroComprobante(requestBody.getNumeroComprobante())
                .notasAdicionales(requestBody.getNotasAdicionales())
                .confirmacionEjecucionServicio(requestBody.isConfirmacionEjecucionServicio())
                .build());

        return ConfirmacionRecepcionPagoResponse.fromEntity(confirmacionRegistered);
    }

}
