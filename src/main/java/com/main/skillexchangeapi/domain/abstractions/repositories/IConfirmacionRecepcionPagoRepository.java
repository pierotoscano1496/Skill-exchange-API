package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.detail.ConfirmacionRecepcionPago;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

public interface IConfirmacionRecepcionPagoRepository {
    ConfirmacionRecepcionPago registrar(ConfirmacionRecepcionPago confirmacion)
            throws DatabaseNotWorkingException, NotCreatedException;
}
