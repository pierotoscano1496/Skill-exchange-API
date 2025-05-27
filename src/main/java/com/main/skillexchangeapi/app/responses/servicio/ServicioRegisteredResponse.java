package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ServicioRegisteredResponse {
    private UUID id;
    private UUID idProveedor;
    private String titulo;
    private String descripcion;
    private double precio;
    private double precioMaximo;
    private double precioMinimo;
    private String tipoPrecio;
    private String ubicacion;
    private String modalidad;
    private boolean aceptaTerminos;
    private List<ServicioSkillResponse> skills;
    private List<ServicioDisponibilidadResponse> disponibilidades;
    private List<ModalidadPagoResponse> modalidadesPago;
    private List<RecursoMultimediaAsignado> recursosMultimedia;
}
