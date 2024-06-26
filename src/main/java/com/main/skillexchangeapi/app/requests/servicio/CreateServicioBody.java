package com.main.skillexchangeapi.app.requests.servicio;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateServicioBody {
    private String titulo;
    private String descripcion;
    private double precio;
    private UUID idUsuario;
    private UUID idSkill;
    //private List<ModalidadPagoBody> modalidadesPago;
    //private List<RecursoMultimediaBody> recursosMultimedia;
}
