package com.main.skillexchangeapi.app.requests.servicio;

import java.util.List;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ServicioConstants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateServicioBody {
    private String titulo;
    private String descripcion;
    private Double precio;
    private ServicioConstants.TipoPrecio tipoPrecio;
    private Double precioMinimo;
    private Double precioMaximo;
    private List<String> urlRecursosMultimediaToDelete;
    private List<ModalidadPagoBody> modalidadesPago;
}
