package com.main.skillexchangeapi.app.responses.servicio;

import com.main.skillexchangeapi.app.constants.ServicioConstants.Modalidad;
import com.main.skillexchangeapi.app.constants.ServicioConstants.TipoPrecio;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.domain.entities.RecursoMultimediaServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
public class ServicioResponse {
        private UUID id;
        private UsuarioResponse proveedor;
        private String titulo;
        private String descripcion;
        private double precio;
        private double precioMaximo;
        private double precioMinimo;
        private TipoPrecio tipoPrecio;
        private String ubicacion;
        private Modalidad modalidad;
        private boolean aceptaTerminos;
        private List<ServicioDisponibilidadResponse> disponibilidades;
        private List<ServicioSkillResponse> skills;
        private List<ModalidadPagoResponse> modalidadesPago;
        private List<RecursoMultimediaServicioResponse> recursosMultimedia;
        private String urlImagePreview;

        public static ServicioResponse fromEntity(Servicio servicio) {
                if (servicio == null) {
                        return null;
                }

                List<ServicioDisponibilidadResponse> disponibilidades = null;
                if (servicio.getDisponibilidades() != null && !servicio.getDisponibilidades().isEmpty()) {
                        disponibilidades = servicio.getDisponibilidades().stream()
                                        .map(ServicioDisponibilidadResponse::fromEntity)
                                        .toList();
                }

                List<ServicioSkillResponse> skills = null;
                if (servicio.getServicioSkills() != null && !servicio.getServicioSkills().isEmpty()) {
                        skills = servicio.getServicioSkills().stream()
                                        .map(ServicioSkillResponse::fromEntity)
                                        .toList();
                }

                List<RecursoMultimediaServicioResponse> recursosMultimedia = null;
                if (servicio.getRecursosMultimediaServicio() != null
                                && !servicio.getRecursosMultimediaServicio().isEmpty()) {
                        recursosMultimedia = servicio.getRecursosMultimediaServicio().stream()
                                        .map(RecursoMultimediaServicioResponse::fromEntity)
                                        .toList();
                }
                String urlImagePreview = null;
                if (recursosMultimedia != null && !recursosMultimedia.isEmpty()) {
                        urlImagePreview = recursosMultimedia.get(0).getUrl();
                }

                return ServicioResponse.builder()
                                .id(servicio.getId())
                                .proveedor(UsuarioResponse.fromEntity(servicio.getProveedor()))
                                .titulo(servicio.getTitulo())
                                .descripcion(servicio.getDescripcion())
                                .precio(servicio.getPrecio())
                                .precioMaximo(servicio.getPrecioMaximo())
                                .precioMinimo(servicio.getPrecioMinimo())
                                .tipoPrecio(servicio.getTipoPrecio())
                                .ubicacion(servicio.getUbicacion())
                                .modalidad(servicio.getModalidad())
                                .aceptaTerminos(servicio.isAceptaTerminos())
                                .disponibilidades(disponibilidades)
                                .skills(skills)
                                .modalidadesPago(servicio.getModalidadesPago() != null
                                                ? servicio.getModalidadesPago().stream()
                                                                .map(ModalidadPagoResponse::fromEntity)
                                                                .toList()
                                                : null)
                                .urlImagePreview(urlImagePreview)
                                .recursosMultimedia(recursosMultimedia)
                                .build();
        }
}
