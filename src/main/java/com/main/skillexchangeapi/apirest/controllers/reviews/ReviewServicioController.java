package com.main.skillexchangeapi.apirest.controllers.reviews;

import com.main.skillexchangeapi.app.responses.reviews.comentarios.ComentarioServicioResponse;
import com.main.skillexchangeapi.app.responses.reviews.servicio.ServicioReviewResponse;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IComentarioServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IReviewServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;

@RestController
@RequestMapping(value = "servicios/review", produces = "application/json")
public class ReviewServicioController {
    @Autowired
    private IReviewServicioService service;

    @GetMapping("/{idServicio}")
    public ServicioReviewResponse review(@PathVariable UUID idServicio) {
        return service.review(idServicio);
    }
}
