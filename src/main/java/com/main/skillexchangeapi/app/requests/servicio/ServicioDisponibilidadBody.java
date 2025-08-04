package com.main.skillexchangeapi.app.requests.servicio;

import java.time.LocalTime;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ServicioConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServicioDisponibilidadBody {
    private UUID idServicio;
    private ServicioConstants.Dia dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
