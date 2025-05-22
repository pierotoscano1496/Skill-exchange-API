package com.main.skillexchangeapi.domain.abstractions.repositories;

import java.util.List;

import com.main.skillexchangeapi.domain.entities.detail.ServicioImagen;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;

public interface IServicioImagenRepository {
    List<ServicioImagen> registrarMultiple(List<ServicioImagen> servicioImagenes) throws DatabaseNotWorkingException, NotCreatedException;
}
