package com.main.skillexchangeapi.app.requests.servicio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import com.main.skillexchangeapi.app.constants.ServicioConstants;

@Getter
@Setter
@NoArgsConstructor
public class CreateServicioBody {
    private String titulo;
    private String descripcion;
    private double precio;
    private UUID idProveedor;
    private ServicioConstants.TipoPrecio tipoPrecio;
    private double precioMinimo;
    private double precioMaximo;
    private String ubicacion;
    private ServicioConstants.Modalidad modalidad;
    private boolean aceptaTerminos;
    private List<ServicioSkillBody> skills;
    private List<ServicioDisponibilidadBody> disponibilidades;
    private List<ModalidadPagoBody> modalidadesPago;
}
