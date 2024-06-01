package com.main.skillexchangeapi.app.responses.servicio;

import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.usuario.CategoriaResponse;
import com.main.skillexchangeapi.app.responses.usuario.SubCategoriaResponse;
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
    private SkillResponse skill;
    private SubCategoriaResponse subCategoria;
    private CategoriaResponse categoria;
    private List<MedioPagoResponse> modalidadesPago;
    private List<RecursoMultimediaResponse> recursosMultimedia;
}
