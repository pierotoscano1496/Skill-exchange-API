package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.requests.servicio.*;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.servicio.*;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IModalidadPagoRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IRecursoMultimediaServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IServicioService;
import com.main.skillexchangeapi.domain.entities.*;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.entities.searchparameters.SearchServicioParams;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServicioService implements IServicioService {
    @Autowired
    private IServicioRepository repository;

    @Autowired
    private IModalidadPagoRepository modalidadPagoRepository;

    @Autowired
    private IRecursoMultimediaServicioRepository recursoMultimediaServicioRepository;

    @Override
    public List<ServicioBusquedaResponse> searchByParameters(SearchServiciosParametersBody requestBody) throws DatabaseNotWorkingException, ResourceNotFoundException {
        return repository.searchByParams(SearchServicioParams.builder()
                        .keyWord(requestBody.getKeyWord())
                        .idSkill(requestBody.getIdSkill())
                        .idSubcategoria(requestBody.getIdSubcategoria())
                        .idCategoria(requestBody.getIdCategoria())
                        .build())
                .stream().map(s -> ServicioBusquedaResponse.builder()
                        .id(s.getId())
                        .descripcion(s.getDescripcion())
                        .precio(s.getPrecio())
                        .titulo(s.getTitulo())
                        .idUsuario(s.getSkillUsuario().getUsuario().getId())
                        .nombresUsuario(s.getSkillUsuario().getUsuario().getNombres())
                        .apellidosUsuario(s.getSkillUsuario().getUsuario().getApellidos())
                        .correoUsuario(s.getSkillUsuario().getUsuario().getCorreo())
                        .idSkill(s.getSkillUsuario().getSkill().getId())
                        .descripcionSkill(s.getSkillUsuario().getSkill().getDescripcion())
                        .build()).collect(Collectors.toList());
    }

    @Override
    public ServicioDetailsPreviewResponse obtenerDetailsPreview(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        Servicio servicio = repository.obtenerDetails(id);
        List<RecursoMultimediaServicio> recursosMultimediaServicio = recursoMultimediaServicioRepository.obtenerByServicio(id);
        List<ModalidadPago> modalidadesPago = modalidadPagoRepository.obtenerByServicio(id);

        return ServicioDetailsPreviewResponse.builder()
                .id(servicio.getId())
                .descripcion(servicio.getDescripcion())
                .titulo(servicio.getTitulo())
                .precio(servicio.getPrecio())
                .prestamista(UsuarioResponse.builder()
                        .id(servicio.getSkillUsuario().getUsuario().getId())
                        .dni(servicio.getSkillUsuario().getUsuario().getDni())
                        .carnetExtranjeria(servicio.getSkillUsuario().getUsuario().getCarnetExtranjeria())
                        .tipoDocumento(servicio.getSkillUsuario().getUsuario().getTipoDocumento())
                        .nombres(servicio.getSkillUsuario().getUsuario().getNombres())
                        .apellidos(servicio.getSkillUsuario().getUsuario().getApellidos())
                        .correo(servicio.getSkillUsuario().getUsuario().getCorreo())
                        .fechaNacimiento(servicio.getSkillUsuario().getUsuario().getFechaNacimiento())
                        .introduccion(servicio.getSkillUsuario().getUsuario().getIntroduccion())
                        .perfilFacebook(servicio.getSkillUsuario().getUsuario().getPerfilFacebook())
                        .perfilInstagram(servicio.getSkillUsuario().getUsuario().getPerfilInstagram())
                        .perfilLinkedin(servicio.getSkillUsuario().getUsuario().getPerfilLinkedin())
                        .perfilTiktok(servicio.getSkillUsuario().getUsuario().getPerfilTiktok())
                        .build())
                .recursosMultimedia(recursosMultimediaServicio.stream()
                        .map(r -> RecursoMultimediaResponse.builder()
                                .id(r.getId())
                                .medio(r.getMedio())
                                .url(r.getUrl())
                                .build())
                        .collect(Collectors.toList()))
                .modalidadesPago(modalidadesPago.stream()
                        .map(m -> MedioPagoResponse.builder()
                                .id(m.getId())
                                .numeroCelular(m.getNumeroCelular())
                                .tipo(m.getTipo())
                                .cuentaBancaria(m.getCuentaBancaria())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public ServicioRegisteredResponse registrar(CreateServicioBody requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        Servicio servicioRegistered = repository.registrar(Servicio.builder()
                .titulo(requestBody.getTitulo())
                .descripcion(requestBody.getDescripcion())
                .precio(requestBody.getPrecio())
                .skillUsuario(SkillUsuario.builder()
                        .usuario(Usuario.builder()
                                .id(requestBody.getIdUsuario())
                                .build())
                        .skill(Skill.builder()
                                .id(requestBody.getIdSkill())
                                .build())
                        .build())
                .build());

        List<ModalidadPago> modalidadesPagoRegistered = modalidadPagoRepository
                .registrarMultiple(requestBody
                        .getModalidadesPago().stream().map(m -> ModalidadPago.builder()
                                .tipo(m.getTipo())
                                .cuentaBancaria(m.getCuentaBancaria())
                                .numeroCelular(m.getNumeroCelular())
                                .servicio(servicioRegistered)
                                .build())
                        .collect(Collectors.toList()));

        List<RecursoMultimediaServicio> recursosMultimediaServicioRegistered = recursoMultimediaServicioRepository
                .registrarMultiple(requestBody
                        .getRecursosMultimedia().stream().map(r -> RecursoMultimediaServicio.builder()
                                .medio(r.getMedio())
                                .url(r.getUrl())
                                .servicio(servicioRegistered)
                                .build())
                        .collect(Collectors.toList()));

        return ServicioRegisteredResponse.builder()
                .id(servicioRegistered.getId())
                .titulo(servicioRegistered.getTitulo())
                .descripcion(servicioRegistered.getDescripcion())
                .precio(servicioRegistered.getPrecio())
                .idSkill(servicioRegistered.getSkillUsuario().getSkill().getId())
                .idUsuario(servicioRegistered.getSkillUsuario().getUsuario().getId())
                .modalidadesPago(modalidadesPagoRegistered)
                .recursosMultimediaServicio(recursosMultimediaServicioRegistered)
                .build();
    }

    @Override
    public ServicioModalidadesPagoAsignadosResponse asignarModalidadesPago(UUID id, List<AsignacionModalidadPagoToServicioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        List<ModalidadPago> modalidadesPago = requestBody.stream().map(m -> ModalidadPago.builder()
                        .id(UuidManager.randomUuid())
                        .tipo(m.getTipo())
                        .servicio(Servicio.builder()
                                .id(id).build())
                        .cuentaBancaria(m.getCuentaBancaria())
                        .numeroCelular(m.getNumeroCelular())
                        .build())
                .collect(Collectors.toList());

        return ServicioModalidadesPagoAsignadosResponse.builder()
                .id(id)
                .modalidadesPagoAsignado(modalidadPagoRepository.registrarMultiple(modalidadesPago).stream().map(m ->
                        ModalidadPagoAsignado.builder()
                                .id(m.getId())
                                .tipo(m.getTipo())
                                .cuentaBancaria(m.getCuentaBancaria())
                                .numeroCelular(m.getNumeroCelular())
                                .build()).toList())
                .build();
    }

    @Override
    public ServicioRecursosMultimediaAsignadosResponse asignarRecursosMultimedia(UUID id, List<AsignacionRecursoMultimediaToServicioRequest> requestBody) throws DatabaseNotWorkingException, NotCreatedException {
        List<RecursoMultimediaServicio> recursosMultimediaServicio = requestBody.stream().map(r -> RecursoMultimediaServicio.builder()
                        .id(UuidManager.randomUuid())
                        .medio(r.getMedio())
                        .servicio(Servicio.builder()
                                .id(id)
                                .build())
                        .url(r.getUrl())
                        .build())
                .collect(Collectors.toList());

        return ServicioRecursosMultimediaAsignadosResponse.builder()
                .id(id)
                .recursosMultimediaAsignados(recursoMultimediaServicioRepository.registrarMultiple(recursosMultimediaServicio).stream().map(r ->
                                RecursoMultimediaAsignado.builder()
                                        .id(r.getId())
                                        .url(r.getUrl())
                                        .medio(r.getMedio())
                                        .build())
                        .toList())
                .build();
    }
}
