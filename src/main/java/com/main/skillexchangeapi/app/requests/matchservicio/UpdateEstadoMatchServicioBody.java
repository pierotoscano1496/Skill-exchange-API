package com.main.skillexchangeapi.app.requests.matchservicio;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants;

import lombok.Getter;

@Getter
public class UpdateEstadoMatchServicioBody {
    private MatchServicioConstants.Estado estado;
}
