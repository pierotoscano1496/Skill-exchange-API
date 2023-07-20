package com.main.skillexchangeapi.domain.abstractions.services;

import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import java.util.ArrayList;

public interface IPlanService {
    ArrayList<Plan> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException;

    Plan obtenerPlanGratuito() throws DatabaseNotWorkingException, ResourceNotFoundException;

    Plan obtenerByCodigo(String codigo) throws DatabaseNotWorkingException, ResourceNotFoundException;
}
