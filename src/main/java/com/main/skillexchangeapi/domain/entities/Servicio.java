package com.main.skillexchangeapi.domain.entities;

import com.main.skillexchangeapi.domain.entities.detail.ServicioDisponibilidad;
import com.main.skillexchangeapi.domain.entities.detail.ServicioImagen;
import com.main.skillexchangeapi.domain.entities.detail.ServicioSkill;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Servicio {
    private final UUID id;
    private Usuario proveedor;
    private String titulo;
    private String descripcion;
    private double precio;
    private double precioMaximo;
    private double precioMinimo;
    private String tipoPrecio;
    private String ubicacion;
    private String modalidad;
    private boolean aceptaTerminos;
    private List<ModalidadPago> modalidadesPago;
    private List<RecursoMultimediaServicio> recursosMultimediaServicio;
    private List<ServicioDisponibilidad> disponibilidades;
    private List<ServicioSkill> skills;
}
