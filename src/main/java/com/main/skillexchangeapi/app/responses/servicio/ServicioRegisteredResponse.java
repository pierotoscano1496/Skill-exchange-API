package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ServicioRegisteredResponse {
    private UUID id;
    private String titulo;
    private String descripcion;
    private double precio;
    private UUID idUsuario;
    private UUID idSkill;
    private List<ServicioSkillResponse> skills;
    private List<ServicioDisponibilidadResponse> disponibilidades;
    private List<ServicioImagenResponse> imagenes;
    //private List<ModalidadPago> modalidadesPago;
    //private List<RecursoMultimediaServicio> recursosMultimediaServicio;
}
