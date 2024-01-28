package com.main.skillexchangeapi.domain.entities;

import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
public class Servicio {
    private final UUID id;
    private SkillUsuario skillUsuario;
    private String titulo;
    private String descripcion;
    private double precio;
    private ArrayList<ModalidadPago> modalidadesPago;
}
