package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.searchparameters.SearchServicioParams;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.List;

public interface IServicioRepository {
    List<Servicio> searchByParams(SearchServicioParams params)throws DatabaseNotWorkingException, ResourceNotFoundException;
    Servicio registrar(Servicio servicio) throws DatabaseNotWorkingException, NotCreatedException;
}
