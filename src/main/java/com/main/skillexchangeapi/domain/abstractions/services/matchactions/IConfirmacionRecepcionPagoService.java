package com.main.skillexchangeapi.domain.abstractions.services.matchactions;

import com.main.skillexchangeapi.app.requests.matchservicio.ConfirmacionRecepcionPagoBody;
import com.main.skillexchangeapi.app.responses.matchservicio.ConfirmacionRecepcionPagoResponse;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

public interface IConfirmacionRecepcionPagoService {
    ConfirmacionRecepcionPagoResponse registrar(ConfirmacionRecepcionPagoBody requestBody)
            throws DatabaseNotWorkingException, NotCreatedException;
}
