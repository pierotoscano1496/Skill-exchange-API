package com.main.skillexchangeapi.app.requests.servicio;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class CreateServicioBody {
    private String titulo;
    private String descripcion;
    private double precio;
    private UUID idProveedor;
    private UUID idSkill;
    /**
     * Valores posibles: "fijo", "hora", "rango"
     */
    private String tipoPrecio;
    private double precioMinimo;
    private double precioMaximo;
    private String ubicacion;
    /**
     * Valores posibles: "presencial", "remoto", "mixto"
     */
    private String modalidad;
    private boolean aceptaTerminos;
    private List<ServicioSkillBody> skills;
    private List<ServicioDisponibilidadBody> disponibilidades;
    private List<ModalidadPagoBody> modalidadesPago;
    private List<RecursoMultimediaBody> recursosMultimedia;
}
