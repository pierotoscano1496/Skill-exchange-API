package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.requests.matchservicio.CreateMatchServicioBody;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IMatchServicioService;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchServicioService implements IMatchServicioService {
    @Autowired
    private IMatchServicioRepository repository;

    @Override
    public List<MatchServicioResponse> obtenerFromPrestamistaByOptionalEstado(UUID idPrestamista, String estado) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtenerFromPrestamistaByOptionalEstado(idPrestamista, estado)
                .stream().map(m -> MatchServicioResponse.builder()
                        .id(m.getId())
                        .idCliente(m.getCliente().getId())
                        .fecha(m.getFecha())
                        .fechaInicio(m.getFechaInicio())
                        .fechaCierre(m.getFechaCierre())
                        .estado(m.getEstado())
                        .puntuacion(m.getPuntuacion())
                        .costo(m.getCosto())
                        .idServicio(m.getServicio().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public MatchServicioResponse registrar(CreateMatchServicioBody requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        MatchServicio matchServicioRegistered = repository.registrar(MatchServicio.builder()
                .id(UuidManager.randomUuid())
                .servicio(Servicio.builder()
                        .id(requestBody.getIdServicio())
                        .build())
                .cliente(Usuario.builder()
                        .id(requestBody.getIdCliente())
                        .build())
                .costo(requestBody.getCosto())
                .build());

        return MatchServicioResponse.builder()
                .id(matchServicioRegistered.getId())
                .idServicio(matchServicioRegistered.getServicio().getId())
                .idCliente(matchServicioRegistered.getCliente().getId())
                .costo(matchServicioRegistered.getCosto())
                .fecha(matchServicioRegistered.getFecha())
                .fechaInicio(matchServicioRegistered.getFechaInicio())
                .fechaCierre(matchServicioRegistered.getFechaCierre())
                .build();
    }

    @Override
    public MatchServicioResponse actualizarEstado(UUID id, String estado) throws DatabaseNotWorkingException, NotUpdatedException {
        MatchServicio matchServicioUpdated = repository.actualizarEstado(id, estado);

        return MatchServicioResponse.builder()
                .id(matchServicioUpdated.getId())
                .idServicio(matchServicioUpdated.getServicio().getId())
                .idCliente(matchServicioUpdated.getCliente().getId())
                .costo(matchServicioUpdated.getCosto())
                .estado(matchServicioUpdated.getEstado())
                .puntuacion(matchServicioUpdated.getPuntuacion())
                .fecha(matchServicioUpdated.getFecha())
                .fechaInicio(matchServicioUpdated.getFechaInicio())
                .fechaCierre(matchServicioUpdated.getFechaCierre())
                .build();
    }

    @Override
    public MatchServicioResponse puntuarServicio(UUID id, int puntuacion) throws DatabaseNotWorkingException, NotUpdatedException {
        MatchServicio matchServicioUpdated = repository.puntuarServicio(id, puntuacion);

        return MatchServicioResponse.builder()
                .id(matchServicioUpdated.getId())
                .idServicio(matchServicioUpdated.getServicio().getId())
                .idCliente(matchServicioUpdated.getCliente().getId())
                .costo(matchServicioUpdated.getCosto())
                .estado(matchServicioUpdated.getEstado())
                .puntuacion(matchServicioUpdated.getPuntuacion())
                .fecha(matchServicioUpdated.getFecha())
                .fechaInicio(matchServicioUpdated.getFechaInicio())
                .fechaCierre(matchServicioUpdated.getFechaCierre())
                .build();
    }
}
