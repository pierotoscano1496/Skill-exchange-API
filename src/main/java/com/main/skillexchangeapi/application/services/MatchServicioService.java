package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.app.requests.matchservicio.AcceptMatchServicioBody;
import com.main.skillexchangeapi.app.requests.matchservicio.CreateMatchServicioBody;
import com.main.skillexchangeapi.app.requests.matchservicio.PuntajeServicioRequest;
import com.main.skillexchangeapi.app.requests.matchservicio.UpdateEstadoMatchServicioBody;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioProveedorDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.app.responses.servicio.ModalidadPagoResponse;
import com.main.skillexchangeapi.app.responses.servicio.ServicioResponse;
import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IModalidadPagoRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IMatchServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.useractions.IUserMatchServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.useractions.IUserProveedorMatchServicioService;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.ModalidadPago;
import com.main.skillexchangeapi.domain.entities.RecursoMultimediaServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.exceptions.*;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchServicioService
                implements IMatchServicioService, IUserMatchServicioService, IUserProveedorMatchServicioService {
        @Autowired
        private IMatchServicioRepository repository;

        @Autowired
        private IModalidadPagoRepository modalidadPagoRepository;

        @Autowired
        private TokenUtils tokenUtils;

        @Override
        public List<MatchServicioProveedorDetailsResponse> obtenerDetailsFromCliente(UUID idCliente)
                        throws DatabaseNotWorkingException, ResourceNotFoundException {
                return repository.obtenerDetailsFromCliente(idCliente)
                                .stream()
                                .map(m -> MatchServicioProveedorDetailsResponse.builder()
                                                .id(m.getId())
                                                .proveedor(UsuarioResponse.builder()
                                                                .id(m.getServicio().getProveedor().getId())
                                                                .dni(m.getServicio().getProveedor().getDni())
                                                                .carnetExtranjeria(m.getServicio().getProveedor()
                                                                                .getCarnetExtranjeria())
                                                                .correo(m.getServicio().getProveedor().getCorreo())
                                                                .nombres(m.getServicio().getProveedor().getNombres())
                                                                .apellidos(m.getServicio().getProveedor()
                                                                                .getApellidos())
                                                                .perfilFacebook(m.getServicio().getProveedor()
                                                                                .getPerfilFacebook())
                                                                .perfilInstagram(m.getServicio().getProveedor()
                                                                                .getPerfilInstagram())
                                                                .perfilLinkedin(m.getServicio().getProveedor()
                                                                                .getPerfilLinkedin())
                                                                .perfilTiktok(m.getServicio().getProveedor()
                                                                                .getPerfilTiktok())
                                                                .tipoDocumento(m.getServicio().getProveedor()
                                                                                .getTipoDocumento())
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
        public List<MatchServicioDetailsResponse> obtenerDetailsFromProveedorAndOptionalEstado(UUID idProveedor,
                        Estado estado) throws DatabaseNotWorkingException, ResourceNotFoundException {
                return repository.obtenerDetailsFromProveedorAndOptionalEstado(idProveedor, estado)
                                .stream()
                                .map(m -> MatchServicioDetailsResponse.builder()
                                                .id(m.getId())
                                                .cliente(UsuarioResponse.builder()
                                                                .id(m.getCliente().getId())
                                                                .dni(m.getCliente().getDni())
                                                                .carnetExtranjeria(
                                                                                m.getCliente().getCarnetExtranjeria())
                                                                .correo(m.getCliente().getCorreo())
                                                                .nombres(m.getCliente().getNombres())
                                                                .apellidos(m.getCliente().getApellidos())
                                                                .perfilFacebook(m.getCliente().getPerfilFacebook())
                                                                .perfilInstagram(m.getCliente().getPerfilInstagram())
                                                                .perfilLinkedin(m.getCliente().getPerfilLinkedin())
                                                                .perfilTiktok(m.getCliente().getPerfilTiktok())
                                                                .tipoDocumento(m.getCliente().getTipoDocumento())
                                                                .build())
                                                .fecha(m.getFecha())
                                                .fechaInicio(m.getFechaInicio())
                                                .fechaCierre(m.getFechaCierre())
                                                .estado(m.getEstado())
                                                .puntuacion(m.getPuntuacion())
                                                .costo(m.getCosto())
                                                .mensaje(m.getMensaje())
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
        public List<MatchServicioDetailsResponse> obtenerDetailsFromProveedorInServing(UUID idProveedor)
                        throws DatabaseNotWorkingException, ResourceNotFoundException {
                return repository.obtenerDetailsFromProveedorInServing(idProveedor)
                                .stream().map(m -> MatchServicioDetailsResponse.builder()
                                                .id(m.getId())
                                                .cliente(UsuarioResponse.builder()
                                                                .id(m.getCliente().getId())
                                                                .dni(m.getCliente().getDni())
                                                                .carnetExtranjeria(
                                                                                m.getCliente().getCarnetExtranjeria())
                                                                .correo(m.getCliente().getCorreo())
                                                                .nombres(m.getCliente().getNombres())
                                                                .apellidos(m.getCliente().getApellidos())
                                                                .perfilFacebook(m.getCliente().getPerfilFacebook())
                                                                .perfilInstagram(m.getCliente().getPerfilInstagram())
                                                                .perfilLinkedin(m.getCliente().getPerfilLinkedin())
                                                                .perfilTiktok(m.getCliente().getPerfilTiktok())
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
        public Double obtenerPuntajeFromProveedor(UUID idProveedor) throws DatabaseNotWorkingException {
                try {
                        List<MatchServicio> matchs = repository
                                        .obtenerDetailsFromProveedorAndOptionalEstado(idProveedor,
                                                        Estado.finalizado);

                        return matchs.stream().mapToDouble(MatchServicio::getPuntuacion).average()
                                        .orElse(0.0);
                } catch (ResourceNotFoundException e) {
                        return 0.0;
                }
        }

        @Override
        public MatchServicioResponse registrar(CreateMatchServicioBody requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException {
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
        public MatchServicioResponse aceptarMatch(UUID id, AcceptMatchServicioBody requestBody)
                        throws DatabaseNotWorkingException, NotUpdatedException, ResourceNotFoundException,
                        BadRequestException {
                final Estado[] estadoAvailableForAceptar = { Estado.solicitado };

                // Verificar si el estado para aceptar sea el correcto
                MatchServicio matchServicio = repository.obtener(id);
                if (Arrays.asList(estadoAvailableForAceptar).contains(matchServicio.getEstado())) {
                        return MatchServicioResponse.fromEntity(repository.aceptar(id, requestBody.getFechaInicio()));
                } else {
                        throw new BadRequestException("El match está en estado que ya no se puede aceptar");
                }
        }

        @Override
        public MatchServicioResponse actualizarEstado(UUID id, UpdateEstadoMatchServicioBody requestBody)
                        throws DatabaseNotWorkingException, NotUpdatedException, ResourceNotFoundException,
                        BadRequestException {
                // Corroborar que el estado sea el correcto según el ciclo de vida del match
                MatchServicio matchServicio = repository.obtener(id);
                boolean estadoIsCorrect = false;
                final Estado[] estadosReadyForSolicitado = { Estado.pendiente_pago, Estado.rechazado };
                final Estado estadoReadyForPendientePago = Estado.ejecucion;
                final Estado estadoReadyForEjecucion = Estado.finalizado;
                Estado estado = requestBody.getEstado();

                String servicioClosedMessage = null;

                switch (matchServicio.getEstado()) {
                        case solicitado:
                                if (Arrays.asList(estadosReadyForSolicitado).contains(estado)) {
                                        estadoIsCorrect = true;
                                }
                                break;
                        case pendiente_pago:
                                if (estado.equals(estadoReadyForPendientePago)) {
                                        estadoIsCorrect = true;
                                }
                                break;
                        case ejecucion:
                                if (estado.equals(estadoReadyForEjecucion)) {
                                        estadoIsCorrect = true;
                                }
                                break;
                        case rechazado:
                                servicioClosedMessage = "El servicio ya está rechazado";
                                break;
                        case finalizado:
                                servicioClosedMessage = "El servicio ya finalizó";
                                break;
                }

                if (estadoIsCorrect) {
                        MatchServicio matchServicioUpdated = repository.actualizarEstado(id, estado);

                        return MatchServicioResponse.fromEntity(matchServicioUpdated);
                } else {
                        throw new BadRequestException(
                                        servicioClosedMessage != null ? servicioClosedMessage : "Estado incorrecto");
                }
        }

        @Override
        public MatchServicioResponse puntuarServicio(UUID id, PuntajeServicioRequest request)
                        throws DatabaseNotWorkingException, NotUpdatedException {
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

        @Override
        public List<MatchServicioDetailsResponse> obtenerMatchsFromCliente(HttpServletRequest request)
                        throws ResourceNotFoundException, DatabaseNotWorkingException {
                UUID idUsuario = tokenUtils.extractIdFromRequest(request);

                List<MatchServicio> matchs = repository.obtenerDetailsFromCliente(idUsuario);

                for (MatchServicio match : matchs) {
                        if (match.getEstado() == Estado.pendiente_pago) {
                                List<ModalidadPago> modalidadesPago = modalidadPagoRepository
                                                .obtenerByServicio(match.getServicio().getId());
                                match.getServicio().setModalidadesPago(modalidadesPago);
                        }
                }

                List<MatchServicioDetailsResponse> matchsResponse = MatchServicioDetailsResponse.fromEntity(matchs);

                return matchsResponse;
        }

        @Override
        public boolean checkAvailableMatchForServicio(HttpServletRequest request, UUID idServicio)
                        throws DatabaseNotWorkingException {
                UUID idUsuario = tokenUtils.extractIdFromRequest(request);

                try {
                        List<MatchServicio> matches = repository
                                        .obtenerByServicioAndCliente(idServicio, idUsuario);

                        return matches.stream()
                                        .anyMatch(m -> m.getServicio().getId().equals(idServicio)
                                                        && (m.getEstado() == Estado.solicitado
                                                                        || m.getEstado() == Estado.pendiente_pago
                                                                        || m.getEstado() == Estado.ejecucion));
                } catch (ResourceNotFoundException e) {
                        return false;
                }
        }

        @Override
        public List<MatchServicioDetailsResponse> obtenerMatchsFromProveedor(HttpServletRequest request, Estado estado)
                        throws ResourceNotFoundException, DatabaseNotWorkingException {
                UUID idUsuario = tokenUtils.extractIdFromRequest(request);
                return MatchServicioDetailsResponse
                                .fromEntity(repository.obtenerDetailsFromProveedorAndOptionalEstado(idUsuario, estado));
        }

}
