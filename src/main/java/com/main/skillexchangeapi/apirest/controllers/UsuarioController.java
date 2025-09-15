package com.main.skillexchangeapi.apirest.controllers;

import com.azure.core.annotation.Get;
import com.google.auto.value.AutoValue.Builder;
import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.app.constants.UsuarioConstants.TipoDocumento;
import com.main.skillexchangeapi.app.requests.SetPlanToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.matchservicio.CreateFirstMatchServicioBody;
import com.main.skillexchangeapi.app.requests.usuario.AsignacionSkillToUsuarioRequest;
import com.main.skillexchangeapi.app.requests.usuario.CreateUsuarioBody;
import com.main.skillexchangeapi.app.responses.SkillResponse;
import com.main.skillexchangeapi.app.responses.UsuarioResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioDetailsResponse;
import com.main.skillexchangeapi.app.responses.matchservicio.MatchServicioResponse;
import com.main.skillexchangeapi.app.responses.skill.SkillAsignadoResponse;
import com.main.skillexchangeapi.app.responses.skill.SkillInfoResponse;
import com.main.skillexchangeapi.app.responses.usuario.PlanAsignado;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioRegisteredResponse;
import com.main.skillexchangeapi.app.responses.usuario.UsuarioSkillsAsignadosResponse;
import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.application.services.MatchServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.IMatchServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.IUsuarioService;
import com.main.skillexchangeapi.domain.abstractions.services.useractions.IUserMatchServicioService;
import com.main.skillexchangeapi.domain.abstractions.services.useractions.IUserProveedorMatchServicioService;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotDeletedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "usuario", produces = "application/json")
public class UsuarioController {
    @Autowired
    private IUsuarioService service;

    @Autowired
    private final IMatchServicioService matchServicioService;

    @Autowired
    private IUserMatchServicioService userMatchServicioService;

    @Autowired
    private TokenUtils tokenUtils;

    Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    UsuarioController(MatchServicioService matchServicioService) {
        this.matchServicioService = matchServicioService;
    }

    @GetMapping("/auth")
    public ResponseEntity<UsuarioRegisteredResponse> obtener(HttpServletRequest request) {
        try {
            String correo = tokenUtils.extractEmailFromRequest(request);
            return ResponseEntity.status(HttpStatus.OK).body(service.obtener(correo));
        } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/skills/{id}")
    public List<SkillResponse> obtenerSkills(@PathVariable UUID id) {
        try {
            return service.obtenerSkills(id);
        } catch (DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/own/skills/info")
    public List<SkillInfoResponse> obtenerSkillsInfo(HttpServletRequest request) {
        try {
            String correo = tokenUtils.extractEmailFromRequest(request);
            return service.obtenerSkillsInfo(correo);
        } catch (DatabaseNotWorkingException e) {
            logger.error("Error en GET /own/skills/info: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ResourceNotFoundException e) {
            logger.error("Error en GET /own/skills/info: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/own/skills")
    public List<SkillAsignadoResponse> obtenerSkillsAsignados(HttpServletRequest request) {
        try {
            String correo = tokenUtils.extractEmailFromRequest(request);
            return service.obtenerSkillsAsignados(correo);
        } catch (DatabaseNotWorkingException e) {
            logger.error("Error en GET /own/skills/asignados: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ResourceNotFoundException e) {
            logger.error("Error en GET /own/skills/asignados: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/own/skill/{idSkill}/exists-in-servicios")
    public ResponseEntity<Boolean> checkIfSkillExistsInServicios(@PathVariable UUID idSkill,
            HttpServletRequest request) {
        try {
            String correo = tokenUtils.extractEmailFromRequest(request);
            return ResponseEntity.ok(service.checkIfSkillExistsInServicios(idSkill, correo));
        } catch (DatabaseNotWorkingException e) {
            logger.error("Error en GET /own/skill/{}/exists-in-servicios", idSkill, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    @PostMapping
    public ResponseEntity<UsuarioRegisteredResponse> registrar(@RequestBody CreateUsuarioBody requestBody) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(requestBody));
        } catch (NotCreatedException | EncryptionAlghorithmException | DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/plan/{id}")
    public ResponseEntity<PlanAsignado> asignarPlan(@PathVariable UUID id, SetPlanToUsuarioRequest requestBody) {
        try {
            PlanAsignado planUsuarioAssigned = service.asignarPlan(id, requestBody);
            return ResponseEntity.ok(planUsuarioAssigned);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Gestión de skills para usuario autenticado
    @PatchMapping("/skills/{id}")
    public UsuarioSkillsAsignadosResponse asignarSkills(@PathVariable UUID id,
            @RequestBody List<AsignacionSkillToUsuarioRequest> requestBody) {
        try {
            return service.asignarSkills(id, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/own/skills")
    public UsuarioSkillsAsignadosResponse asignarSkills(@RequestBody List<AsignacionSkillToUsuarioRequest> requestBody,
            HttpServletRequest request) {
        try {
            String correo = tokenUtils.extractEmailFromRequest(request);
            return service.asignarSkills(correo, requestBody);
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PatchMapping("/own/skill")
    public ResponseEntity<SkillAsignadoResponse> asignarSkill(@RequestBody AsignacionSkillToUsuarioRequest requestBody,
            HttpServletRequest request) {
        try {
            String correo = tokenUtils.extractEmailFromRequest(request);
            return ResponseEntity.ok(service.asignarSkill(correo, requestBody));
        } catch (DatabaseNotWorkingException | NotCreatedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkIfExists(@RequestParam(required = false) TipoDocumento tipoDocumento,
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String correo) {
        try {
            return ResponseEntity.ok(service.existsBy(tipoDocumento, documento, correo));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de documento invalido");
        } catch (DatabaseNotWorkingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    @DeleteMapping("/own/skill/{idSkill}")
    public ResponseEntity<Boolean> deleteSkillFromUsuario(@PathVariable UUID idSkill, HttpServletRequest request) {
        try {
            String correo = tokenUtils.extractEmailFromRequest(request);
            return ResponseEntity.ok(service.deleteSkil(idSkill, correo));
        } catch (DatabaseNotWorkingException | NotDeletedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RestController
    @RequestMapping(value = "usuario/own/match", produces = "application/json")
    public static class UserActionMatchController {
        @Autowired
        private IUsuarioService service;

        @Autowired
        private IUserMatchServicioService userMatchServicioService;

        @GetMapping("/available/{idServicio}")
        public boolean checkAvailableMatchForServicio(HttpServletRequest request, @PathVariable UUID idServicio) {
            try {
                return userMatchServicioService.checkAvailableMatchForServicio(request, idServicio);
            } catch (DatabaseNotWorkingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }

        @PostMapping
        public MatchServicioResponse registrarMatch(@RequestBody CreateFirstMatchServicioBody requestBody,
                HttpServletRequest request) {
            try {
                return service.registrarMatch(requestBody, request);
            } catch (ResourceNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } catch (DatabaseNotWorkingException | NotCreatedException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @RestController
    @RequestMapping(value = "usuario/proveedor/match", produces = "application/json")
    public static class UserProveedorMatchController {
        @Autowired
        private IUserProveedorMatchServicioService userProveedorMatchServicioService;

        @GetMapping({ "", "/", "/{estado}" })
        public List<MatchServicioDetailsResponse> obtener(HttpServletRequest request,
                @PathVariable(required = false) Estado estado) {
            try {
                return userProveedorMatchServicioService.obtenerMatchsFromProveedor(request, estado);
            } catch (ResourceNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
            } catch (DatabaseNotWorkingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Profile("dev")
    @RestController
    @RequestMapping(value = "usuario", produces = "application/json")
    public static class UsuarioDevController {
        @Autowired
        private IUsuarioService service;

        @GetMapping
        public List<UsuarioResponse> obtenerTodos() {
            try {
                return service.obtenerTodos();
            } catch (DatabaseNotWorkingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }

        @Getter
        @Builder
        public static class RestorePasswordBody {
            private String password;

            public String getPassword() {
                return password;
            }
        }

        @PatchMapping("/restore/password/{id}")
        public ResponseEntity<UsuarioResponse> restorePassword(@PathVariable UUID id,
                @RequestBody Map<String, Object> body) {
            try {
                return ResponseEntity.ok(service.restorePassword(id, body));
            } catch (DatabaseNotWorkingException | ResourceNotFoundException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
