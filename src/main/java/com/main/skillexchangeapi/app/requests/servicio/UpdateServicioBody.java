package com.main.skillexchangeapi.app.requests.servicio;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.main.skillexchangeapi.app.constants.ServicioConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateServicioBody {
    private String titulo;
    private String descripcion;
    private Double precio;
    private ServicioConstants.TipoPrecio tipoPrecio;
    private Double precioMinimo;
    private Double precioMaximo;
    private List<UUID> idRecursosMultimediaToDelete;
    private List<UUID> idModalidadesPagoToDelete;
    private List<ModalidadPagoBody> modalidadesPago;
}
