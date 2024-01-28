package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.requests.AsignacionModalidadPagoToServicioRequest;
import com.main.skillexchangeapi.app.requests.AsignacionRecursoMultimediaToServicioRequest;
import com.main.skillexchangeapi.app.responses.ModalidadPagoAsignadoToServicioResponse;
import com.main.skillexchangeapi.app.responses.RecursosMultimediaAsignadoToServicioResponse;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IModalidadPagoRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IRecursoMultimediaServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IServicioService;
import com.main.skillexchangeapi.domain.entities.ModalidadPago;
import com.main.skillexchangeapi.domain.entities.RecursoMultimediaServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServicioService implements IServicioService {
    @Autowired
    private IServicioRepository repository;

    @Autowired
    private IModalidadPagoRepository modalidadPagoRepository;

    @Autowired
    private IRecursoMultimediaServicioRepository recursoMultimediaServicioRepository;

    @Override
    public Servicio registrar(Servicio servicio) throws DatabaseNotWorkingException, NotCreatedException {
        return repository.registrar(servicio);
    }

    @Override
    public List<ModalidadPagoAsignadoToServicioResponse> asignarModalidadesPago(UUID id, List<AsignacionModalidadPagoToServicioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        List<ModalidadPago> modalidadesPago = requestBody.stream().map(m -> ModalidadPago.builder()
                        .id(UuidManager.randomUuid())
                        .tipo(m.getTipo())
                        .servicio(Servicio.builder()
                                .id(id).build())
                        .cuentaBancaria(m.getCuentaBancaria())
                        .numeroCelular(m.getNumeroCelular())
                        .build())
                .collect(Collectors.toList());
        List<ModalidadPago> modalidadesPagoRegistered = modalidadPagoRepository.registrarMultiple(modalidadesPago);

        return modalidadesPagoRegistered.stream().map(m -> ModalidadPagoAsignadoToServicioResponse.builder()
                        .idServicio(m.getId())
                        .tipo(m.getTipo())
                        .idServicio(m.getServicio().getId())
                        .cuentaBancaria(m.getCuentaBancaria())
                        .numeroCelular(m.getNumeroCelular())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<RecursosMultimediaAsignadoToServicioResponse> asignarRecursosMultimedia(UUID id, List<AsignacionRecursoMultimediaToServicioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        List<RecursoMultimediaServicio> recursosMultimediaServicio = requestBody.stream().map(r -> RecursoMultimediaServicio.builder()
                        .id(UuidManager.randomUuid())
                        .medio(r.getMedio())
                        .servicio(Servicio.builder()
                                .id(id)
                                .build())
                        .url(r.getUrl())
                        .build())
                .collect(Collectors.toList());

        List<RecursoMultimediaServicio> recursosMultimediaServicioRegistered = recursoMultimediaServicioRepository.registrarMultiple(recursosMultimediaServicio);

        return recursosMultimediaServicioRegistered.stream().map(r -> RecursosMultimediaAsignadoToServicioResponse.builder()
                        .id(r.getId())
                        .url(r.getUrl())
                        .medio(r.getMedio())
                        .idServicio(r.getServicio().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
