package com.main.skillexchangeapi.app.requests.matchservicio;


import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Usuario;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class CreateMatchServicioBody {
    private UUID idServicio;
    private UUID idCliente;
    private LocalDate fecha;
    private LocalDate fechaInicio;
    private LocalDate fechaCierre;
    private int puntuacion;
    private double costo;
}
