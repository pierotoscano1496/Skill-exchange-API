package com.main.skillexchangeapi.apirest.controllers;

import com.main.skillexchangeapi.app.requests.servicio.AsignacionModalidadPagoToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.AsignacionRecursoMultimediaToServicioRequest;
import com.main.skillexchangeapi.app.requests.servicio.SearchServiciosParametersBody;
import com.main.skillexchangeapi.app.requests.servicio.CreateServicioBody;
import com.main.skillexchangeapi.app.responses.servicio.*;
import com.main.skillexchangeapi.domain.abstractions.services.IServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IAWSS3ServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IBlobStorageModalidadPagoService;
import com.main.skillexchangeapi.domain.abstractions.services.storage.IBlobStorageRecursoMultimediaServicioService;
import com.main.skillexchangeapi.domain.constants.PaymentMethod;
import com.main.skillexchangeapi.domain.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class ServicioController {
    @Autowired
    private IServicioService service;

    @Autowired
    private IAWSS3ServicioService storageService;

    @GetMapping("/usuario/{idUsuario}")
    private List<ServicioResponse> obtenerByUsuario(@PathVariable UUID idUsuario) {
        try {
            List<ServicioResponse> servicios = service.obtenerByUsuario(idUsuario).stream().map(servicio -> {
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
    public ServicioDetailsPreviewResponse obtenerDetailsPreview(@PathVariable UUID id) {
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
    public String obtenerImagenMetodoPago(@PathVariable UUID id, @PathVariable PaymentMethod paymentMethod) {
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
    private List<ServicioBusquedaResponse> searchByParameters(@RequestBody SearchServiciosParametersBody parameters) {
        try {
            return service.searchByParameters(parameters);
        } catch (DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new ArrayList<>();
        }
    }

    @PostMapping
    private ServicioRegisteredResponse registrar(@RequestBody CreateServicioBody requestBody) {
        try {
            return service.registrar(requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/modalidad-pago/{id}")
    private ServicioModalidadesPagoAsignadosResponse asignarModalidadesPago(@PathVariable UUID id, @RequestBody List<AsignacionModalidadPagoToServicioRequest> requestBody) {
        try {
            return service.asignarModalidadesPago(id, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/upload-multimedia/{id}")
    private List<MultimediaResourceUploadedResponse> uploadMultimediaServiceResource(@PathVariable UUID id, @RequestParam("files") List<MultipartFile> files) {
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
    private String uploadMetadataModalidadPagoToService(@PathVariable UUID id, @PathVariable PaymentMethod paymentMethod, @RequestParam("file") MultipartFile file) {
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
    private ServicioRecursosMultimediaAsignadosResponse asignarRecursosMultimedia(@PathVariable UUID id, @RequestBody List<AsignacionRecursoMultimediaToServicioRequest> requestBody) {
        try {
            return service.asignarRecursosMultimedia(id, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
