package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;
import com.main.skillexchangeapi.app.requests.servicio.AsignacionModalidadPagoToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.AsignacionRecursoMultimediaToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.SearchServiciosParametersBody;
import com.main.skillexchangeapi.app.requests.servicio.UpdateServicioBody;
import com.main.skillexchangeapi.app.requests.servicio.CreateServicioBody;
import com.main.skillexchangeapi.app.responses.servicio.*;
import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.domain.abstractions.services.IServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IAWSS3ServicioService;
import com.main.skillexchangeapi.domain.constants.PaymentMethod;
import com.main.skillexchangeapi.domain.exceptions.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "servicio", produces = "application/json")
@Tag(name = "Servicio", description = "CRUD de los servicios")
public class ServicioController {
    @Autowired
    private IServicioService service;

    @Autowired
    private IAWSS3ServicioService storageService;

    @Autowired
    private TokenUtils tokenUtils;

    Logger logger = LoggerFactory.getLogger(ServicioController.class);

    @GetMapping("/proveedor")
    @Operation(summary = "Obtener servicios por usuario", description = "Devuelve los servicios creados por el usuario del sistema")
    public List<ServicioResponse> obtenerByUsuario(HttpServletRequest request) {
        try {
            UUID idUsuario = tokenUtils.extractIdFromRequest(request);
            List<ServicioResponse> servicios = service.obtenerByProveedor(idUsuario).stream().map(servicio -> {
                try {
                    servicio.setUrlImagePreview(storageService.getFirstImageServicioPresignedUrl(servicio.getId()));
                } catch (FileNotFoundException e) {
                    servicio.setUrlImagePreview(null);
                }

                return servicio;
            }).collect(Collectors.toList());

            return servicios;
        } catch (ResourceNotFoundException | DatabaseNotWorkingException e) {
            HttpStatus statusError = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof DatabaseNotWorkingException) {
                statusError = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(statusError, e.getMessage());
        }
    }

    @GetMapping("details/preview/{id}")
    @Operation(summary = "Obtener detalles para previsualización por ID", description = "Muestra información de un servicio particular sin mucho detalle, solo para previsualización")
    public ServicioResponse obtenerDetailsPreview(@PathVariable UUID id) {
        try {
            return service.obtenerDetailsPreview(id);
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;

            if (e instanceof ResourceNotFoundException) {
                errorStatus = HttpStatus.NOT_FOUND;
            }
            throw new ResponseStatusException(errorStatus, e.getMessage());
        }
    }

    @GetMapping("/payment-method/image/{id}/{paymentMethod}")
    @Operation(summary = "Obtener imágenes del método de pago por ID del servicio y el tipo de método de pago", description = "Busca un método de pago desde el bucket que corresponde a un servicio y especificando un tipo")
    public String obtenerImagenMetodoPago(@PathVariable UUID id,
            @PathVariable ModalidadPagoConstants.Tipo paymentMethod) {
        try {
            return storageService.getImageMetodoPagoPresignedUrl(id, paymentMethod);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Búsquedas personalizadas
     */
    @PostMapping("busqueda")
    @Operation(summary = "Busca servicios a través de parámetros de diversos", description = "Buscar servicios por: palabra clave, ID de habilidad, ID de subcategoría o ID de categoría")
    public List<ServicioResponse> searchByParameters(@RequestBody SearchServiciosParametersBody parameters) {
        try {
            return service.searchByParameters(parameters);
        } catch (DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Guarda un servicio con su metadata", description = "Guarda primero los archivos de los recursos multimedia de un servicio a S3: imágenes, videos cortos, métodos de pago, etc. y la información del servicio adicionalmente con: habilidades, disponibilidades y modalidades de pago")
    public ServicioRegisteredResponse registrar(@RequestPart("data") CreateServicioBody requestBody,
            @RequestPart(value = "multimedia", required = false) List<MultipartFile> recursosMultimedia,
            @RequestPart(value = "yapeMultimedia", required = false) MultipartFile yapeFile) {
        try {
            ServicioRegisteredResponse response = service.registrar(requestBody, recursosMultimedia, yapeFile);
            logger.info("Registro de servicio exitoso. ID generado: {}", response.getId());
            return response;
        } catch (DatabaseNotWorkingException | NotCreatedException | IOException | InvalidFileException
                | FileNotUploadedException e) {
            if (e instanceof IOException) {
                logger.error("Error al registrar el servicio: {}", e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar el servicio");
            } else {
                logger.error("Respuesta de error del servicio: {}", e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualiza un servicio por su ID", description = "Actualiza la información de un servicio en específico: título, descripción, tipo de precio, precio, métodos de pago.")
    public ServicioResponse actualizar(@PathVariable UUID id, @RequestPart("data") UpdateServicioBody requestBody,
            @RequestPart(value = "multimedia", required = false) List<MultipartFile> recursosMultimedia,
            @RequestPart(value = "yapeMultimedia", required = false) MultipartFile yapeFile) {
        try {
            return service.actualizar(id, requestBody, recursosMultimedia, yapeFile);
        } catch (DatabaseNotWorkingException | NotUpdatedException | IOException | InvalidFileException
                | FileNotUploadedException | FileNotFoundException | ResourceNotFoundException
                | NotDeletedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/modalidad-pago/{id}")
    @Operation(summary = "Asigna modalidades de pago a un servicio", description = "Mediante el ID del servicio, se le asigna las modalidades de pago")
    public ServicioModalidadesPagoAsignadosResponse asignarModalidadesPago(@PathVariable UUID id,
            @RequestBody List<AsignacionModalidadPagoToServicioRequest> requestBody) {
        try {
            return service.asignarModalidadesPago(id, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/upload-multimedia/{id}")
    @Operation(summary = "Asigna archivos multimedia a un servicio", description = "Agrega archivos a S3 y después los registros, tales como su url")
    public List<MultimediaResourceUploadedResponse> uploadMultimediaServiceResource(@PathVariable UUID id,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            return storageService.uploadMultimediaServiceResources(id, files);
        } catch (IOException | InvalidFileException | FileNotUploadedException e) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof InvalidFileException) {
                errorStatus = HttpStatus.BAD_REQUEST;
            }
            throw new ResponseStatusException(errorStatus, e.getMessage());
        }
    }

    @PatchMapping("/upload-metadata-modalidad-pago/{id}/{paymentMethod}")
    @Operation(summary = "Agrega archivos multimedia a una modalidad de pago de un servicio", description = "Agrega archivos a la modalidad de pago de un servicio de un servicio por su ID")
    public String uploadMetadataModalidadPagoToService(@PathVariable UUID id,
            @PathVariable ModalidadPagoConstants.Tipo paymentMethod, @RequestParam("file") MultipartFile file) {
        try {
            return storageService.uploadModalidadPagoResource(id, paymentMethod, file);
        } catch (IOException | InvalidFileException | FileNotUploadedException e) {
            HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof InvalidFileException) {
                errorStatus = HttpStatus.BAD_REQUEST;
            }
            throw new ResponseStatusException(errorStatus, e.getMessage());
        }
    }

    @PatchMapping("recursos-multimedia/{id}")
    @Operation(summary = "Asigna recursos multimedia a un servicio", description = "Agrega los archivos de los recursos a S3 y las anexa al servicio indicado por su ID")
    public ServicioRecursosMultimediaAsignadosResponse asignarRecursosMultimedia(@PathVariable UUID id,
            @RequestBody List<AsignacionRecursoMultimediaToServicioRequest> requestBody) {
        try {
            return service.asignarRecursosMultimedia(id, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
