package com.main.skillexchangeapi.domain.entities;

import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import lombok.Data;

@Data
public class Servicio {
    private final Long id;
    private SkillUsuario skillUsuario;
    private String descripcion;
    private double precio;
    private ModalidadPago modalidadPago;
}
