package com.main.skillexchangeapi.domain.abstractions.services.useractions;

import java.util.List;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioDetailsResponse;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

public interface IUserMatchServicioService {
    List<MatchServicioDetailsResponse> obtenerMatchsFromCliente(HttpServletRequest request)
            throws ResourceNotFoundException, DatabaseNotWorkingException;

    boolean checkAvailableMatchForServicio(HttpServletRequest request, UUID idServicio)
            throws DatabaseNotWorkingException;
}
