package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.ModalidadPago;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IModalidadPagoRepository {
    List<ModalidadPago> obtenerByServicio(UUID idServicio)
            throws ResourceNotFoundException, DatabaseNotWorkingException;

    ModalidadPago obtener(UUID id) throws ResourceNotFoundException, DatabaseNotWorkingException;

    ModalidadPago registrar(ModalidadPago modalidadPago) throws DatabaseNotWorkingException, NotCreatedException;

    List<ModalidadPago> registrarMultiple(List<ModalidadPago> modalidadesPago)
            throws DatabaseNotWorkingException, NotCreatedException;
}
