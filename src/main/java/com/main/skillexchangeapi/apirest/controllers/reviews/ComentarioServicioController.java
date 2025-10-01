package com.main.skillexchangeapi.apirest.controllers.reviews;

import com.main.skillexchangeapi.app.requests.review.comentarios.ComentarioServicioBody;
import com.main.skillexchangeapi.app.responses.reviews.comentarios.ComentarioServicioResponse;
import com.main.skillexchangeapi.domain.abstractions.services.reviews.IComentarioServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "servicios/comments", produces = "application/json")
public class ComentarioServicioController {
    @Autowired
    private IComentarioServicioService service;

    @PostMapping("/publish")
    public ComentarioServicioResponse publicar(@RequestBody ComentarioServicioBody requestBBody) {
        return service.publicar(requestBBody);
    }
}
