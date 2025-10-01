package com.main.skillexchangeapi.domain.abstractions.services.useractions;

import java.util.List;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

public interface IUserProveedorMatchServicioService {
    List<MatchServicioDetailsResponse> obtenerMatchsFromProveedor(HttpServletRequest request, Estado estado)
            throws ResourceNotFoundException, DatabaseNotWorkingException;
}
