package com.main.skillexchangeapi.app.requests.servicio;

import java.util.UUID;

import lombok.Getter;

@Getter
public class ServicioImagenBody {
    private UUID id;
    private UUID idServicio;
    private String urlImagen;
}
