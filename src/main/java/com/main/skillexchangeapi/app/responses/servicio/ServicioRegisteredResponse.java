package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ServicioConstants.Modalidad;
import com.main.skillexchangeapi.app.constants.ServicioConstants.TipoPrecio;

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
    private TipoPrecio tipoPrecio;
    private String ubicacion;
    private Modalidad modalidad;
    private boolean aceptaTerminos;
    private List<ServicioSkillResponse> skills;
    private List<ServicioDisponibilidadResponse> disponibilidades;
    private List<ModalidadPagoResponse> modalidadesPago;
    private List<RecursoMultimediaAsignado> recursosMultimedia;
}
