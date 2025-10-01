package com.main.skillexchangeapi.application.services;

import com.main.skillexchangeapi.app.requests.CreateSkillRequest;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.app.responses.skill.SkillInfoResponse;
import com.main.skillexchangeapi.domain.abstractions.repositories.ICategoriaRepository;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISkillRepository;
import com.main.skillexchangeapi.domain.abstractions.services.ISkillService;
import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.SubCategoria;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SkillService implements ISkillService {
        @Autowired
        private ISkillRepository repository;

        @Autowired
        private ICategoriaRepository categoriaRepository;

        @Override
        public List<SkillResponse> obtener() throws DatabaseNotWorkingException, ResourceNotFoundException {
                return repository.obtener().stream().map(s -> SkillResponse.builder()
                                .id(s.getId())
                                .descripcion(s.getDescripcion())
                                .idSubCategoria(s.getSubCategoria().getId())
                                .build()).collect(Collectors.toList());
        }

        @Override
        public List<SkillInfoResponse> obtenerInfo() throws DatabaseNotWorkingException, ResourceNotFoundException {
                return repository.obtenerInfo().stream().map(s -> SkillInfoResponse.builder()
                                .id(s.getId())
                                .descripcion(s.getDescripcion())
                                .nombreSubCategoria(s.getNombreSubCategoria())
                                .nombreCategoria(s.getNombreCategoria())
                                .build()).collect(Collectors.toList());
        }

        @Override
        public List<SkillResponse> obtenerBySubCategoria(UUID idSubcategoria)
                        throws DatabaseNotWorkingException, ResourceNotFoundException {
                return repository.obtenerBySubCategoria(idSubcategoria).stream().map(s -> SkillResponse.builder()
                                .id(s.getId())
                                .descripcion(s.getDescripcion())
                                .idSubCategoria(s.getSubCategoria().getId())
                                .build()).collect(Collectors.toList());
        }

        @Override
        public SkillResponse registrar(CreateSkillRequest request)
                        throws DatabaseNotWorkingException, NotCreatedException {
                SubCategoria subCategoria = SubCategoria.builder()
                                .id(request.getIdSubcategoria()).build();

                Skill skill = Skill.builder()
                                .descripcion(request.getDescripcion())
                                .subCategoria(subCategoria)
                                .build();

                Skill skillRegistered = repository.registrar(skill);

                return SkillResponse.builder()
                                .id(skillRegistered.getId())
                                .descripcion(skillRegistered.getDescripcion())
                                .idSubCategoria(skillRegistered.getSubCategoria().getId())
                                .build();
        }
}
