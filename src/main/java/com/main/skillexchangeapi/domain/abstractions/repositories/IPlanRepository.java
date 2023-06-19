package com.main.skillexchangeapi.domain.abstractions.repositories;

import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.ArrayList;

public interface IPlanRepository {
    public ArrayList<Plan> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException;

    public Plan obtenerPlanGratuito() throws DatabaseNotWorkingException, ResourceNotFoundException;

    public Plan obtenerByCodigo(String codigo) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
