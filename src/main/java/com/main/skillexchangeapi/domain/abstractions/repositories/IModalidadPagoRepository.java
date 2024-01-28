package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.ModalidadPago;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

import java.util.List;

public interface IModalidadPagoRepository {
    ModalidadPago registrar(ModalidadPago modalidadPago) throws DatabaseNotWorkingException, NotCreatedException;
    List<ModalidadPago> registrarMultiple(List<ModalidadPago> modalidadesPago) throws DatabaseNotWorkingException, NotCreatedException;
}
