package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;
import com.main.skillexchangeapi.app.constants.ServicioConstants.TipoPrecio;
import com.main.skillexchangeapi.app.requests.servicio.*;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.servicio.*;
import com.main.skillexchangeapi.app.responses.usuario.CategoriaResponse;
import com.main.skillexchangeapi.app.responses.usuario.SubCategoriaResponse;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IModalidadPagoRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IRecursoMultimediaServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioDisponibilidadRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioImagenRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioSkillRepository;
import com.main.skillexchangeapi.domain.abstractions.services.IServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IAWSS3ServicioService;
import com.main.skillexchangeapi.domain.constants.PaymentMethod;
import com.main.skillexchangeapi.domain.entities.*;
import com.main.skillexchangeapi.domain.entities.detail.ServicioDisponibilidad;
import com.main.skillexchangeapi.domain.entities.detail.ServicioImagen;
import com.main.skillexchangeapi.domain.entities.detail.ServicioSkill;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.entities.searchparameters.SearchServicioParams;
import com.main.skillexchangeapi.domain.exceptions.BadRequestException;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.FileNotUploadedException;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ServicioService implements IServicioService {
        @Autowired
        private IServicioRepository repository;

        @Autowired
        private IModalidadPagoRepository modalidadPagoRepository;

        @Autowired
        private IRecursoMultimediaServicioRepository recursoMultimediaServicioRepository;

        @Autowired
        private IServicioSkillRepository servicioSkillRepository;

        @Autowired
        private IServicioDisponibilidadRepository servicioDisponibilidadRepository;

        @Autowired
        private IServicioImagenRepository servicioImagenRepository;

        @Autowired
        private IAWSS3ServicioService storageService;

        Logger logger = LoggerFactory.getLogger(ServicioService.class);

        @Override
        public List<ServicioResponse> obtenerByProveedor(UUID idProveedor)
                        throws DatabaseNotWorkingException, ResourceNotFoundException {
                return repository.obtenerByProveedor(idProveedor).stream().map(s -> ServicioResponse.builder()
                                .id(s.getId())
                                .descripcion(s.getDescripcion())
                                .precio(s.getPrecio())
                                .precioMinimo(s.getPrecioMinimo())
                                .precioMaximo(s.getPrecioMaximo())
                                .titulo(s.getTitulo())
                                .tipoPrecio(s.getTipoPrecio())
                                .ubicacion(s.getUbicacion())
                                .modalidad(s.getModalidad())
                                .aceptaTerminos(s.isAceptaTerminos())
                                .build()).collect(Collectors.toList());
        }

        @Override
        public List<ServicioResponse> searchByParameters(SearchServiciosParametersBody requestBody)
                        throws DatabaseNotWorkingException, ResourceNotFoundException {
                return repository.searchByParams(SearchServicioParams.builder()
                                .keyWord(requestBody.getKeyWord())
                                .idSkill(requestBody.getIdSkill())
                                .idSubcategoria(requestBody.getIdSubcategoria())
                                .idCategoria(requestBody.getIdCategoria())
                                .build())
                                .stream().map(s -> ServicioResponse.builder()
                                                .id(s.getId())
                                                .descripcion(s.getDescripcion())
                                                .precio(s.getPrecio())
                                                .tipoPrecio(s.getTipoPrecio())
                                                .precioMinimo(s.getPrecioMinimo())
                                                .precioMaximo(s.getPrecioMaximo())
                                                .titulo(s.getTitulo())
                                                .ubicacion(s.getUbicacion())
                                                .modalidad(s.getModalidad())
                                                .aceptaTerminos(s.isAceptaTerminos())
                                                .proveedor(UsuarioResponse.builder()
                                                                .id(s.getProveedor().getId())
                                                                .nombres(s.getProveedor().getNombres())
                                                                .apellidos(s.getProveedor().getApellidos())
                                                                .build())
                                                .build())
                                .collect(Collectors.toList());
        }

        @Override
        public ServicioResponse obtenerDetailsPreview(UUID id)
                        throws DatabaseNotWorkingException, ResourceNotFoundException {
                Servicio servicio = repository.obtenerDetails(id);
                List<RecursoMultimediaServicio> recursosMultimediaServicio;
                try {
                        recursosMultimediaServicio = recursoMultimediaServicioRepository.obtenerByServicio(id);
                } catch (ResourceNotFoundException e) {
                        recursosMultimediaServicio = new ArrayList<>();
                }

                List<ModalidadPago> modalidadesPago = modalidadPagoRepository.obtenerByServicio(id);

                return ServicioResponse.builder()
                                .id(servicio.getId())
                                .titulo(servicio.getTitulo())
                                .descripcion(servicio.getDescripcion())
                                .precio(servicio.getPrecio())
                                .tipoPrecio(servicio.getTipoPrecio())
                                .precioMinimo(servicio.getPrecioMinimo())
                                .precioMaximo(servicio.getPrecioMaximo())
                                .ubicacion(servicio.getUbicacion())
                                .modalidad(servicio.getModalidad())
                                .aceptaTerminos(servicio.isAceptaTerminos())
                                .proveedor(UsuarioResponse.builder()
                                                .id(servicio.getProveedor().getId())
                                                .dni(servicio.getProveedor().getDni())
                                                .carnetExtranjeria(servicio.getProveedor().getCarnetExtranjeria())
                                                .tipoDocumento(servicio.getProveedor().getTipoDocumento())
                                                .nombres(servicio.getProveedor().getNombres())
                                                .apellidos(servicio.getProveedor().getApellidos())
                                                .correo(servicio.getProveedor().getCorreo())
                                                .fechaNacimiento(servicio.getProveedor().getFechaNacimiento())
                                                .introduccion(servicio.getProveedor().getIntroduccion())
                                                .perfilFacebook(servicio.getProveedor().getPerfilFacebook())
                                                .perfilInstagram(servicio.getProveedor().getPerfilInstagram())
                                                .perfilLinkedin(servicio.getProveedor().getPerfilLinkedin())
                                                .perfilTiktok(servicio.getProveedor().getPerfilTiktok())
                                                .build())
                                .skills(servicio.getServicioSkills().stream()
                                                .map(s -> ServicioSkillResponse.builder()
                                                                .skill(SkillResponse.builder()
                                                                                .id(s.getSkill().getId())
                                                                                .descripcion(s.getSkill()
                                                                                                .getDescripcion())
                                                                                .subCategoria(SubCategoriaResponse
                                                                                                .builder()
                                                                                                .id(s.getSkill().getSubCategoria()
                                                                                                                .getId())
                                                                                                .nombre(s.getSkill()
                                                                                                                .getSubCategoria()
                                                                                                                .getNombre())
                                                                                                .categoria(CategoriaResponse
                                                                                                                .builder()
                                                                                                                .id(s.getSkill()
                                                                                                                                .getSubCategoria()
                                                                                                                                .getCategoria()
                                                                                                                                .getId())
                                                                                                                .nombre(s.getSkill()
                                                                                                                                .getSubCategoria()
                                                                                                                                .getCategoria()
                                                                                                                                .getNombre())
                                                                                                                .build())
                                                                                                .build())
                                                                                .build())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .modalidadesPago(modalidadesPago.stream()
                                                .map(m -> ModalidadPagoResponse.builder()
                                                                .id(m.getId())
                                                                .numeroCelular(m.getNumeroCelular())
                                                                .tipo(m.getTipo())
                                                                .cuentaBancaria(m.getCuentaBancaria())
                                                                .url(m.getUrl())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .disponibilidades(servicio.getDisponibilidades().stream()
                                                .map(d -> ServicioDisponibilidadResponse.builder()
                                                                .id(d.getId())
                                                                .dia(d.getDia())
                                                                .horaInicio(d.getHoraInicio())
                                                                .horaFin(d.getHoraFin())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .build();
        }

        @Override
        public ServicioRegisteredResponse registrar(CreateServicioBody requestBody,
                        List<MultipartFile> recursosMultimedia,
                        MultipartFile yapeFile)
                        throws DatabaseNotWorkingException, NotCreatedException, IOException, InvalidFileException,
                        FileNotUploadedException {
                UUID idServicio = UuidManager.randomUuid();
                logger.info("Recibiendo solicitud para registrar un servicio con los siguientes datos: {}",
                                requestBody);

                /*
                 * Evaluar lista de recursos multimedia y filtrar los que no están vacíos
                 */
                if (recursosMultimedia != null && !recursosMultimedia.isEmpty()) {
                        int numEmptyFiles = 0;
                        for (MultipartFile multipartFile : recursosMultimedia) {
                                if (multipartFile == null || multipartFile.isEmpty()) {
                                        logger.warn("Archivo multimedia vacío recibido");
                                        numEmptyFiles++;
                                } else {
                                        logger.info("Archivo multimedia recibido: {} ({} bytes)",
                                                        multipartFile.getOriginalFilename(),
                                                        multipartFile.getSize());
                                }
                        }

                        if (numEmptyFiles > 0) {
                                logger.warn("{} archivos multimedia vacíos fueron ignorados", numEmptyFiles);
                        }

                        recursosMultimedia = recursosMultimedia.stream()
                                        .filter(file -> file != null && !file.isEmpty())
                                        .collect(Collectors.toList());
                }
                logger.info("Iniciando registro de servicio. Datos recibidos: {}", requestBody);

                // Guardar las imágenes de previsualización en el bucket de S3
                List<MultimediaResourceUploadedResponse> resourceUploaded = storageService
                                .uploadMultimediaServiceResources(idServicio, recursosMultimedia);

                // Guardar las imágenes de Yape en el bucket de S3
                String resourceYapeUploaded = storageService
                                .uploadModalidadPagoResource(idServicio, ModalidadPagoConstants.Tipo.yape, yapeFile);

                // Asignar la URL de Yape al requestBody
                for (ModalidadPagoBody modalidadPagoBody : requestBody.getModalidadesPago()) {
                        if (modalidadPagoBody.getTipo() == ModalidadPagoConstants.Tipo.yape) {
                                modalidadPagoBody.setUrl(resourceYapeUploaded);
                                break;
                        }
                }

                Servicio servicioRegistered = repository.registrar(Servicio.builder()
                                .id(idServicio)
                                .proveedor(Usuario.builder()
                                                .id(requestBody.getIdProveedor())
                                                .build())
                                .titulo(requestBody.getTitulo())
                                .descripcion(requestBody.getDescripcion())
                                .precio(requestBody.getPrecio())
                                .precioMaximo(requestBody.getPrecioMaximo())
                                .precioMinimo(requestBody.getPrecioMinimo())
                                .tipoPrecio(requestBody.getTipoPrecio())
                                .ubicacion(requestBody.getUbicacion())
                                .modalidad(requestBody.getModalidad())
                                .aceptaTerminos(requestBody.isAceptaTerminos())
                                .servicioSkills(requestBody.getSkills()
                                                .stream()
                                                .map(s -> ServicioSkill.builder()
                                                                .skill(Skill.builder()
                                                                                .id(s.getIdSkill())
                                                                                .build())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .modalidadesPago(requestBody.getModalidadesPago()
                                                .stream()
                                                .map(m -> ModalidadPago.builder()
                                                                .tipo(m.getTipo())
                                                                .cuentaBancaria(m.getCuentaBancaria())
                                                                .numeroCelular(m.getNumeroCelular())
                                                                .url(m.getUrl())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .recursosMultimediaServicio(resourceUploaded
                                                .stream()
                                                .map(r -> RecursoMultimediaServicio.builder()
                                                                .id(UuidManager.randomUuid())
                                                                .medio(r.getMedio())
                                                                .url(r.getUrl())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .disponibilidades(requestBody.getDisponibilidades()
                                                .stream()
                                                .map(d -> ServicioDisponibilidad.builder()
                                                                .dia(d.getDia())
                                                                .horaInicio(d.getHoraInicio())
                                                                .horaFin(d.getHoraFin())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .build());

                return ServicioRegisteredResponse.builder()
                                .id(servicioRegistered.getId())
                                .idProveedor(servicioRegistered.getProveedor().getId())
                                .titulo(servicioRegistered.getTitulo())
                                .descripcion(servicioRegistered.getDescripcion())
                                .precio(servicioRegistered.getPrecio())
                                .precioMinimo(servicioRegistered.getPrecioMinimo())
                                .precioMaximo(servicioRegistered.getPrecioMaximo())
                                .ubicacion(servicioRegistered.getUbicacion())
                                .modalidad(servicioRegistered.getModalidad())
                                .aceptaTerminos(servicioRegistered.isAceptaTerminos())
                                .skills(servicioRegistered.getServicioSkills().stream()
                                                .map(s -> ServicioSkillResponse.builder()
                                                                .idSkill(s.getSkill().getId())
                                                                .idServicio(servicioRegistered.getId())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .disponibilidades(servicioRegistered.getDisponibilidades()
                                                .stream()
                                                .map(s -> ServicioDisponibilidadResponse.builder()
                                                                .id(s.getId())
                                                                .dia(s.getDia())
                                                                .horaInicio(s.getHoraInicio())
                                                                .horaFin(s.getHoraFin())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .modalidadesPago(servicioRegistered.getModalidadesPago()
                                                .stream()
                                                .map(m -> ModalidadPagoResponse.builder()
                                                                .id(m.getId())
                                                                .tipo(m.getTipo())
                                                                .cuentaBancaria(m.getCuentaBancaria())
                                                                .numeroCelular(m.getNumeroCelular())
                                                                .url(m.getUrl())
                                                                .build())
                                                .collect(Collectors.toList()))
                                .build();

        }

        @Override
        public ServicioResponse actualizar(UUID id,
                        UpdateServicioBody requestBody)
                        throws DatabaseNotWorkingException, NotUpdatedException, BadRequestException {
                // validar entradas
                if (requestBody.getTipoPrecio() == TipoPrecio.rango) {
                        if (requestBody.getPrecioMinimo() <= 0 || requestBody.getPrecioMaximo() <= 0
                                        || requestBody.getPrecioMinimo() >= requestBody.getPrecioMaximo()) {
                                throw new BadRequestException("Rango de precios inválido");
                        }
                        requestBody.setPrecio(null);
                } else {
                        if (requestBody.getPrecio() <= 0) {
                                throw new BadRequestException("Precio inválido");
                        }
                        requestBody.setPrecioMinimo(null);
                        requestBody.setPrecioMaximo(null);
                }

                return ServicioResponse.fromEntity(repository.actualizar(Servicio.builder()
                                .id(id)
                                .titulo(requestBody.getTitulo())
                                .descripcion(requestBody.getDescripcion())
                                .precio(requestBody.getPrecio())
                                .tipoPrecio(requestBody.getTipoPrecio())
                                .precioMinimo(requestBody.getPrecioMinimo())
                                .precioMaximo(requestBody.getPrecioMaximo())
                                .build()));

        }

        @Override
        public ServicioModalidadesPagoAsignadosResponse asignarModalidadesPago(UUID id,
                        List<AsignacionModalidadPagoToServicioRequest> requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException {
                List<ModalidadPago> modalidadesPago = requestBody.stream().map(m -> ModalidadPago.builder()
                                .id(UuidManager.randomUuid())
                                .tipo(m.getTipo())
                                .servicio(Servicio.builder()
                                                .id(id).build())
                                .cuentaBancaria(m.getCuentaBancaria())
                                .numeroCelular(m.getNumeroCelular())
                                .url(m.getUrl())
                                .build())
                                .collect(Collectors.toList());

                return ServicioModalidadesPagoAsignadosResponse.builder()
                                .id(id)
                                .modalidadesPagoAsignado(modalidadPagoRepository.registrarMultiple(modalidadesPago)
                                                .stream().map(m -> ModalidadPagoAsignado.builder()
                                                                .id(m.getId())
                                                                .tipo(m.getTipo())
                                                                .cuentaBancaria(m.getCuentaBancaria())
                                                                .numeroCelular(m.getNumeroCelular())
                                                                .url(m.getUrl())
                                                                .build())
                                                .toList())
                                .build();
        }

        @Override
        public ServicioRecursosMultimediaAsignadosResponse asignarRecursosMultimedia(UUID id,
                        List<AsignacionRecursoMultimediaToServicioRequest> requestBody)
                        throws DatabaseNotWorkingException, NotCreatedException {
                List<RecursoMultimediaServicio> recursosMultimediaServicio = requestBody
                                .stream().map(r -> RecursoMultimediaServicio.builder()
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
                                .recursosMultimediaAsignados(recursoMultimediaServicioRepository
                                                .registrarMultiple(recursosMultimediaServicio).stream()
                                                .map(r -> RecursoMultimediaAsignado.builder()
                                                                .id(r.getId())
                                                                .url(r.getUrl())
                                                                .medio(r.getMedio())
                                                                .build())
                                                .toList())
                                .build();
        }
}
