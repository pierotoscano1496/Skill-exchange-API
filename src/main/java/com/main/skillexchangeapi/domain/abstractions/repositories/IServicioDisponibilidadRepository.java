package com.main.skillexchangeapi.domain.abstractions.repositories;

import java.util.List;

import com.main.skillexchangeapi.domain.entities.detail.ServicioDisponibilidad;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

public interface IServicioDisponibilidadRepository {
    List<ServicioDisponibilidad> registrarMultiple(List<ServicioDisponibilidad> servicioDisponibilidades) throws DatabaseNotWorkingException, NotCreatedException;
}
