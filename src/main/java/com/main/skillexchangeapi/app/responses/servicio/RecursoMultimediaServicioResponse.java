package com.main.skillexchangeapi.app.responses.servicio;

import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;
import com.main.skillexchangeapi.domain.entities.RecursoMultimediaServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RecursoMultimediaServicioResponse {
    private final UUID id;
    private ServicioResponse servicio;
    private String url;
    private Medio medio;

    public static RecursoMultimediaServicioResponse fromEntity(RecursoMultimediaServicio recursoMultimediaServicio) {
        if (recursoMultimediaServicio == null) {
            return null;
        }
        Servicio servicio = recursoMultimediaServicio.getServicio();

        return RecursoMultimediaServicioResponse.builder()
                .id(recursoMultimediaServicio.getId())
                .servicio(servicio != null ? ServicioResponse.fromEntity(servicio) : null)
                .url(recursoMultimediaServicio.getUrl())
                .medio(recursoMultimediaServicio.getMedio())
                .build();
    }
}
