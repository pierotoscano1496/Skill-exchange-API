package com.main.skillexchangeapi.domain.entities.restructure;

import java.util.List;

import com.google.auto.value.AutoValue.Builder;
import com.main.skillexchangeapi.domain.entities.ModalidadPago;
import com.main.skillexchangeapi.domain.entities.RecursoMultimediaServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Builder
@Data
public class ServicioUpdate extends Servicio {
    private List<RecursoMultimediaServicio> recursosMultimediaServicioToDelete;
    private List<ModalidadPago> modalidadesPagoToDelete;
}
