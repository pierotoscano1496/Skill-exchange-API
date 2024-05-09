package com.main.skillexchangeapi.app.responses.servicio;

import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ServicioDetailsPreviewResponse {
    private UUID id;
    private String titulo;
    private String descripcion;
    private double precio;
    private UsuarioResponse prestamista;
    private List<MedioPagoResponse> modalidadesPago;
    private List<RecursoMultimediaResponse> recursosMultimedia;
}
