package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

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
    //private List<ModalidadPago> modalidadesPago;
    //private List<RecursoMultimediaServicio> recursosMultimediaServicio;
}
