package com.main.skillexchangeapi.app.responses.servicio;

import com.main.skillexchangeapi.app.responses.UsuarioResponse;
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
    private String tipoPrecio;
    private String ubicacion;
    private String modalidad;
    private boolean aceptaTerminos;
    private List<ServicioDisponibilidadResponse> disponibilidades;
    private List<ServicioSkillResponse> skills;
    private List<ModalidadPagoResponse> modalidadesPago;
    private String urlImagePreview;
}
