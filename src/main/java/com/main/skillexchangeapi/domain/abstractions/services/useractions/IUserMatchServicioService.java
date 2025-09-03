package com.main.skillexchangeapi.domain.abstractions.services.useractions;

import java.util.UUID;

import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;

import jakarta.servlet.http.HttpServletRequest;

public interface IUserMatchServicioService {
    boolean checkAvailableMatchForServicio(HttpServletRequest request, UUID idServicio)
            throws DatabaseNotWorkingException;
}
