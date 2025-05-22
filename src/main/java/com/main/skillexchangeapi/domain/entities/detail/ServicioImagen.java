package com.main.skillexchangeapi.domain.entities.detail;

import java.util.UUID;

import com.main.skillexchangeapi.domain.entities.Servicio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServicioImagen {
    private UUID id;
    private Servicio servicio;
    private String urlImagen;
}
