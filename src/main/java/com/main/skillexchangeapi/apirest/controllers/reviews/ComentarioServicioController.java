package com.main.skillexchangeapi.apirest.controllers.reviews;

import com.main.skillexchangeapi.app.responses.reviews.comentarios.ComentarioServicioResponse;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IComentarioServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "comentarios/servicio", produces = "application/json")
public class ComentarioServicioController {
    @Autowired
    private IComentarioServicioService service;

    @GetMapping("/{idServicio}")
    public List<ComentarioServicioResponse> obtenerPorServicio(@PathVariable UUID idServicio) {
        return service.obtenerByServicio(idServicio);
    }
}
