package com.main.skillexchangeapi.app.requests.matchservicio;

import java.util.UUID;

import lombok.Getter;

@Getter
public class CreateFirstMatchServicioBody {
    protected UUID idServicio;
    protected String mensaje;
}
