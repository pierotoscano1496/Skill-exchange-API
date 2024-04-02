package com.main.skillexchangeapi.manipulation;

import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.entities.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class CollectionSearches {
    private HashMap<UUID, Categoria> categoriaCollections;
    private HashMap<UUID, Usuario> usuarioCollections;

    @BeforeEach
    void setup() {
        categoriaCollections = new HashMap<>();

        Categoria musica = Categoria.builder()
                .nombre("Música")
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))
                .build();
        Categoria educacion = Categoria.builder()
                .nombre("Educación")
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                .build();
        Categoria arte = Categoria.builder()
                .nombre("Arte")
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                .build();

        categoriaCollections.put(musica.getId(), musica);
        categoriaCollections.put(educacion.getId(), educacion);
        categoriaCollections.put(arte.getId(), arte);
    }

    @Test
    void findMusica() {
        Categoria musica = categoriaCollections.get(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        Assertions.assertEquals("Música", musica.getNombre());
    }
}
