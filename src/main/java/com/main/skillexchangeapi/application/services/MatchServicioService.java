package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.requests.matchservicio.CreateMatchServicioBody;
import com.main.skillexchangeapi.app.requests.matchservicio.PuntajeServicioRequest;
import com.main.skillexchangeapi.app.requests.matchservicio.UpdateEstadoMatchServicioBody;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioProveedorDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioResponse;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IMatchServicioService;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchServicioService implements IMatchServicioService {
    @Autowired
    private IMatchServicioRepository repository;

    @Override
    public List<MatchServicioProveedorDetailsResponse> obtenerDetailsFromCliente(UUID idCliente) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtenerDetailsFromCliente(idCliente)
                .stream().map(m -> MatchServicioProveedorDetailsResponse.builder()
                        .id(m.getId())
                        .proveedor(UsuarioResponse.builder()
                                .id(m.getServicio().getSkillUsuario().getUsuario().getId())
                                .dni(m.getServicio().getSkillUsuario().getUsuario().getDni())
                                .carnetExtranjeria(m.getServicio().getSkillUsuario().getUsuario().getCarnetExtranjeria())
                                .correo(m.getServicio().getSkillUsuario().getUsuario().getCorreo())
                                .nombres(m.getServicio().getSkillUsuario().getUsuario().getNombres())
                                .apellidos(m.getServicio().getSkillUsuario().getUsuario().getApellidos())
                                .perfilFacebook(m.getServicio().getSkillUsuario().getUsuario().getPerfilFacebook())
                                .perfilInstagram(m.getServicio().getSkillUsuario().getUsuario().getPerfilInstagram())
                                .perfilLinkedin(m.getServicio().getSkillUsuario().getUsuario().getPerfilLinkedin())
                                .perfilTiktok(m.getServicio().getSkillUsuario().getUsuario().getPerfilTiktok())
                                .tipo(m.getServicio().getSkillUsuario().getUsuario().getTipo())
                                .tipoDocumento(m.getServicio().getSkillUsuario().getUsuario().getTipoDocumento())
                                .build())
                        .fecha(m.getFecha())
                        .fechaInicio(m.getFechaInicio())
                        .fechaCierre(m.getFechaCierre())
                        .estado(m.getEstado())
                        .puntuacion(m.getPuntuacion())
                        .costo(m.getCosto())
                        .servicio(ServicioResponse.builder()
                                .id(m.getServicio().getId())
                                .descripcion(m.getServicio().getDescripcion())
                                .precio(m.getServicio().getPrecio())
                                .titulo(m.getServicio().getTitulo())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchServicioDetailsResponse> obtenerDetailsFromPrestamistaByOptionalEstado(UUID idPrestamista, String estado) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtenerDetailsFromPrestamistaByOptionalEstado(idPrestamista, estado)
                .stream().map(m -> MatchServicioDetailsResponse.builder()
                        .id(m.getId())
                        .cliente(UsuarioResponse.builder()
                                .id(m.getCliente().getId())
                                .dni(m.getCliente().getDni())
                                .carnetExtranjeria(m.getCliente().getCarnetExtranjeria())
                                .correo(m.getCliente().getCorreo())
                                .nombres(m.getCliente().getNombres())
                                .apellidos(m.getCliente().getApellidos())
                                .perfilFacebook(m.getCliente().getPerfilFacebook())
                                .perfilInstagram(m.getCliente().getPerfilInstagram())
                                .perfilLinkedin(m.getCliente().getPerfilLinkedin())
                                .perfilTiktok(m.getCliente().getPerfilTiktok())
                                .tipo(m.getCliente().getTipo())
                                .tipoDocumento(m.getCliente().getTipoDocumento())
                                .build())
                        .fecha(m.getFecha())
                        .fechaInicio(m.getFechaInicio())
                        .fechaCierre(m.getFechaCierre())
                        .estado(m.getEstado())
                        .puntuacion(m.getPuntuacion())
                        .costo(m.getCosto())
                        .servicio(ServicioResponse.builder()
                                .id(m.getServicio().getId())
                                .descripcion(m.getServicio().getDescripcion())
                                .precio(m.getServicio().getPrecio())
                                .titulo(m.getServicio().getTitulo())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchServicioDetailsResponse> obtenerDetailsFromPrestamistaInServing(UUID idPrestamista) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.obtenerDetailsFromPrestamistaInServing(idPrestamista)
                .stream().map(m -> MatchServicioDetailsResponse.builder()
                        .id(m.getId())
                        .cliente(UsuarioResponse.builder()
                                .id(m.getCliente().getId())
                                .dni(m.getCliente().getDni())
                                .carnetExtranjeria(m.getCliente().getCarnetExtranjeria())
                                .correo(m.getCliente().getCorreo())
                                .nombres(m.getCliente().getNombres())
                                .apellidos(m.getCliente().getApellidos())
                                .perfilFacebook(m.getCliente().getPerfilFacebook())
                                .perfilInstagram(m.getCliente().getPerfilInstagram())
                                .perfilLinkedin(m.getCliente().getPerfilLinkedin())
                                .perfilTiktok(m.getCliente().getPerfilTiktok())
                                .tipo(m.getCliente().getTipo())
                                .tipoDocumento(m.getCliente().getTipoDocumento())
                                .build())
                        .fecha(m.getFecha())
                        .fechaInicio(m.getFechaInicio())
                        .fechaCierre(m.getFechaCierre())
                        .estado(m.getEstado())
                        .puntuacion(m.getPuntuacion())
                        .costo(m.getCosto())
                        .servicio(ServicioResponse.builder()
                                .id(m.getServicio().getId())
                                .descripcion(m.getServicio().getDescripcion())
                                .precio(m.getServicio().getPrecio())
                                .titulo(m.getServicio().getTitulo())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public MatchServicioResponse registrar(CreateMatchServicioBody requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        MatchServicio matchServicioRegistered = repository.registrar(MatchServicio.builder()
                .id(UuidManager.randomUuid())
                .servicio(Servicio.builder()
                        .id(requestBody.getIdServicio())
                        .build())
                .cliente(Usuario.builder()
                        .id(requestBody.getIdCliente())
                        .build())
                .costo(requestBody.getCosto())
                .build());

        return MatchServicioResponse.builder()
                .id(matchServicioRegistered.getId())
                .idServicio(matchServicioRegistered.getServicio().getId())
                .idCliente(matchServicioRegistered.getCliente().getId())
                .costo(matchServicioRegistered.getCosto())
                .fecha(matchServicioRegistered.getFecha())
                .fechaInicio(matchServicioRegistered.getFechaInicio())
                .fechaCierre(matchServicioRegistered.getFechaCierre())
                .build();
    }

    @Override
    public MatchServicioResponse actualizarEstado(UUID id, UpdateEstadoMatchServicioBody requestBody) throws DatabaseNotWorkingException, NotUpdatedException, ResourceNotFoundException, BadRequestException {
        // Corroborar que el estado sea el correcto según el ciclo de vida del match
        MatchServicio matchServicio = repository.obtener(id);
        boolean estadoIsCorrect = false;
        final String[] estadosReadyForSolicitado = {"pendiente-pago", "rechazado"};
        final String estadoReadyForPendientePago = "ejecucion";
        final String estadoReadyForEjecucion = "finalizado";
        String estado = requestBody.getEstado();

        String servicioClosedMessage = null;

        switch (matchServicio.getEstado()) {
            case "solicitado":
                if (Arrays.asList(estadosReadyForSolicitado).contains(estado)) {
                    estadoIsCorrect = true;
                }
                break;
            case "pendiente-pago":
                if (estado.equals(estadoReadyForPendientePago)) {
                    estadoIsCorrect = true;
                }
                break;
            case "ejecucion":
                if (estado.equals(estadoReadyForEjecucion)) {
                    estadoIsCorrect = true;
                }
                break;
            case "rechazado":
                servicioClosedMessage = "El servicio ya está rechazado";
                break;
            case "finalizado":
                servicioClosedMessage = "El servicio ya finalizó";
                break;
        }

        if (estadoIsCorrect) {
            MatchServicio matchServicioUpdated = repository.actualizarEstado(id, estado);

            return MatchServicioResponse.builder()
                    .id(matchServicioUpdated.getId())
                    .idServicio(matchServicioUpdated.getServicio().getId())
                    .idCliente(matchServicioUpdated.getCliente().getId())
                    .costo(matchServicioUpdated.getCosto())
                    .estado(matchServicioUpdated.getEstado())
                    .puntuacion(matchServicioUpdated.getPuntuacion())
                    .fecha(matchServicioUpdated.getFecha())
                    .fechaInicio(matchServicioUpdated.getFechaInicio())
                    .fechaCierre(matchServicioUpdated.getFechaCierre())
                    .build();
        } else {
            throw new BadRequestException(servicioClosedMessage != null ? servicioClosedMessage : "Estado incorrecto");
        }
    }

    @Override
    public MatchServicioResponse puntuarServicio(UUID id, PuntajeServicioRequest request) throws DatabaseNotWorkingException, NotUpdatedException {
        int puntaje = request.getPuntaje();
        MatchServicio matchServicioUpdated = repository.puntuarServicio(id, puntaje);

        return MatchServicioResponse.builder()
                .id(matchServicioUpdated.getId())
                .idServicio(matchServicioUpdated.getServicio().getId())
                .idCliente(matchServicioUpdated.getCliente().getId())
                .costo(matchServicioUpdated.getCosto())
                .estado(matchServicioUpdated.getEstado())
                .puntuacion(matchServicioUpdated.getPuntuacion())
                .fecha(matchServicioUpdated.getFecha())
                .fechaInicio(matchServicioUpdated.getFechaInicio())
                .fechaCierre(matchServicioUpdated.getFechaCierre())
                .build();
    }
}
