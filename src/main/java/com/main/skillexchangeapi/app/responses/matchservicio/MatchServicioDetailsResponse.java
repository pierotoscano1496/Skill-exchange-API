package com.main.skillexchangeapi.app.responses.matchservicio;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.servicio.ModalidadPagoResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioResponse;
import com.main.skillexchangeapi.application.services.ServicioService;
import com.main.skillexchangeapi.application.services.UsuarioService;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.ModalidadPago;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Usuario;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
public class MatchServicioDetailsResponse {
    private UUID id;
    private ServicioResponse servicio;
    private UsuarioResponse cliente;
    private LocalDateTime fecha;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCierre;
    private Estado estado;
    private int puntuacion;
    private double costo;
    private String mensaje;

    public static List<MatchServicioDetailsResponse> fromEntity(List<MatchServicio> matchServicios) {
        return matchServicios
                .stream()
                .map(m -> {
                    Usuario cliente = m.getCliente();
                    UsuarioResponse clienteResponse = null;
                    if (cliente != null) {
                        clienteResponse = UsuarioService.mapToUsuarioResponse(cliente);
                    }

                    Servicio servicio = m.getServicio();
                    ServicioResponse servicioResponse = null;
                    if (servicio != null) {
                        servicioResponse = ServicioService.mapToServicioResponse(servicio);

                        Usuario proveedor = servicio.getProveedor();
                        UsuarioResponse proveedorResponse = null;
                        if (proveedor != null) {
                            proveedorResponse = UsuarioService
                                    .mapToUsuarioResponse(proveedor);
                            servicioResponse.setProveedor(proveedorResponse);
                        }

                        List<ModalidadPago> modalidadesPago = servicio.getModalidadesPago();
                        if (modalidadesPago != null && !modalidadesPago.isEmpty()) {
                            servicioResponse.setModalidadesPago(modalidadesPago
                                    .stream()
                                    .map(modalidadPago -> ModalidadPagoResponse
                                            .fromEntity(modalidadPago))
                                    .collect(Collectors.toList()));
                        }
                    }

                    return MatchServicioDetailsResponse.builder()
                            .id(m.getId())
                            .cliente(clienteResponse)
                            .fecha(m.getFecha())
                            .fechaInicio(m.getFechaInicio())
                            .fechaCierre(m.getFechaCierre())
                            .estado(m.getEstado())
                            .puntuacion(m.getPuntuacion())
                            .costo(m.getCosto())
                            .mensaje(m.getMensaje())
                            .servicio(servicioResponse)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
