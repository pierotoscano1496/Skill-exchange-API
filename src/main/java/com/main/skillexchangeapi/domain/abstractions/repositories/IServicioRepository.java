package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IServicioRepository {
    List<Servicio> obtenerServiciosClienteNoRechazados(UUID idCliente) throws DatabaseNotWorkingException, ResourceNotFoundException;

    List<Servicio> obtenerByUsuario(UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException;

    Servicio registrar(Servicio servicio) throws DatabaseNotWorkingException, NotCreatedException;
}
