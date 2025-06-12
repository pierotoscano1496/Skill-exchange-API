package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IMatchServicioRepository {
    List<MatchServicio> obtenerDetailsFromCliente(UUID idCliente) throws DatabaseNotWorkingException, ResourceNotFoundException;

    List<MatchServicio> obtenerDetailsFromPrestamistaByOptionalEstado(UUID idPrestamista, String estado) throws DatabaseNotWorkingException, ResourceNotFoundException;

    List<MatchServicio> obtenerDetailsFromPrestamistaInServing(UUID idPrestamista) throws DatabaseNotWorkingException, ResourceNotFoundException;

    MatchServicio obtener(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException;

    MatchServicio registrar(MatchServicio match) throws DatabaseNotWorkingException, NotCreatedException;

    MatchServicio actualizarEstado(UUID id, Estado estado) throws DatabaseNotWorkingException, NotUpdatedException;

    MatchServicio puntuarServicio(UUID id, int puntuacion) throws DatabaseNotWorkingException, NotUpdatedException;
}
