package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants.Tipo;
import com.main.skillexchangeapi.app.constants.RecursoMultimediaContants.Medio;
import com.main.skillexchangeapi.app.constants.ServicioConstants.Dia;
import com.main.skillexchangeapi.app.constants.ServicioConstants.Modalidad;
import com.main.skillexchangeapi.app.constants.ServicioConstants.TipoPrecio;
import com.main.skillexchangeapi.app.constants.UsuarioConstants.TipoDocumento;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioRepository;
import com.main.skillexchangeapi.domain.entities.*;
import com.main.skillexchangeapi.domain.entities.detail.ServicioDisponibilidad;
import com.main.skillexchangeapi.domain.entities.detail.ServicioSkill;
import com.main.skillexchangeapi.domain.entities.searchparameters.SearchServicioParams;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ServicioRepository implements IServicioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public Servicio obtenerDetails(UUID id) throws ResourceNotFoundException, DatabaseNotWorkingException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("CALL obtener_servicio_details(?)")) {
            statement.setBytes("p_id", UuidManager.UuidToBytes(id));
            boolean hasResult = statement.execute();

            Servicio servicio = null;
            List<ServicioDisponibilidad> disponibilidades = new ArrayList<>();
            List<RecursoMultimediaServicio> recursosMultimedia = new ArrayList<>();
            List<ServicioSkill> servicioSkills = new ArrayList<>();
            int index = 0;

            while (hasResult) {
                try (ResultSet resultSet = statement.getResultSet()) {
                    if (index == 0 && resultSet.next()) {
                        servicio = Servicio.builder()
                                .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                                .descripcion(resultSet.getString("DESCRIPCION"))
                                .precio(resultSet.getDouble("PRECIO"))
                                .precioMaximo(resultSet.getDouble("PRECIO_MAXIMO"))
                                .precioMinimo(resultSet.getDouble("PRECIO_MINIMO"))
                                .titulo(resultSet.getString("TITULO"))
                                .tipoPrecio(TipoPrecio.valueOf(resultSet.getString("TIPO_PRECIO")))
                                .ubicacion(resultSet.getString("UBICACION"))
                                .modalidad(Modalidad.valueOf(resultSet.getString("MODALIDAD")))
                                .aceptaTerminos(resultSet.getBoolean("ACEPTA_TERMINOS"))
                                .proveedor(Usuario.builder()
                                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_PROVEEDOR")))
                                        .nombres(resultSet.getString("NOMBRES_PROVEEDOR"))
                                        .apellidos(resultSet.getString("APELLIDOS_PROVEEDOR"))
                                        .dni(resultSet.getString("DNI_PROVEEDOR"))
                                        .carnetExtranjeria(resultSet.getString("CARNET_EXTRANJERIA_PROVEEDOR"))
                                        .tipoDocumento(
                                                TipoDocumento.valueOf(resultSet.getString("TIPO_DOCUMENTO_PROVEEDOR")))
                                        .correo(resultSet.getString("CORREO_PROVEEDOR"))
                                        .introduccion(resultSet.getString("INTRODUCCION_PROVEEDOR"))
                                        .perfilFacebook(resultSet.getString("PERFIL_FACEBOOK_PROVEEDOR"))
                                        .perfilInstagram(resultSet.getString("PERFIL_INSTAGRAM_PROVEEDOR"))
                                        .perfilLinkedin(resultSet.getString("PERFIL_LINKEDIN_PROVEEDOR"))
                                        .perfilTiktok(resultSet.getString("PERFIL_TIKTOK_PROVEEDOR"))
                                        .build())
                                .build();
                    } else if (index == 1) {
                        while (resultSet.next()) {
                            disponibilidades.add(ServicioDisponibilidad.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                                    .dia(Dia.valueOf(resultSet.getString("DIA")))
                                    .horaInicio(resultSet.getTime("HORA_INICIO").toLocalTime())
                                    .horaFin(resultSet.getTime("HORA_FIN").toLocalTime())
                                    .build());
                        }
                    } else if (index == 2) {
                        while (resultSet.next()) {
                            recursosMultimedia.add(RecursoMultimediaServicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                                    .medio(Medio.valueOf(resultSet.getString("MEDIO")))
                                    .url(resultSet.getString("URL"))
                                    .build());
                        }
                    } else if (index == 3) {
                        while (resultSet.next()) {
                            servicioSkills.add(ServicioSkill.builder()
                                    .skill(Skill.builder()
                                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SKILL")))
                                            .descripcion(resultSet.getString("DESCRIPCION_SKILL"))
                                            .subCategoria(SubCategoria.builder()
                                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SUB_CATEGORIA")))
                                                    .nombre(resultSet.getString("NOMBRE_SUB_CATEGORIA"))
                                                    .categoria(Categoria.builder()
                                                            .id(UuidManager
                                                                    .bytesToUuid(resultSet.getBytes("ID_CATEGORIA")))
                                                            .nombre(resultSet.getString("NOMBRE_CATEGORIA"))
                                                            .build())
                                                    .build())
                                            .build())
                                    .build());
                        }
                    }
                    index++;
                }
                hasResult = statement.getMoreResults();
            }

            if (servicio != null) {
                servicio.setDisponibilidades(disponibilidades);
                servicio.setRecursosMultimediaServicio(recursosMultimedia);
                return servicio;
            } else {
                throw new ResourceNotFoundException("No existe el servicio");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error durante la búsqueda del servicio");
        }
    }

    @Override
    public List<Servicio> obtenerServiciosClienteNoRechazados(UUID idCliente)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        return null;
    }

    @Override
    public List<Servicio> searchByParams(SearchServicioParams params)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("CALL search_servicios(?, ?, ?, ?)")) {
            statement.setString("p_key_word", params.getKeyWord());
            statement.setBytes("p_id_skill",
                    params.getIdSkill() != null ? UuidManager.UuidToBytes(params.getIdSkill()) : null);
            statement.setBytes("p_id_subcategoria",
                    params.getIdSubcategoria() != null ? UuidManager.UuidToBytes(params.getIdSubcategoria()) : null);
            statement.setBytes("p_id_categoria",
                    params.getIdCategoria() != null ? UuidManager.UuidToBytes(params.getIdCategoria()) : null);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Servicio> servicios = new ArrayList<>();

                while (resultSet.next()) {
                    servicios.add(Servicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .proveedor(Usuario.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_PROVEEDOR")))
                                    .nombres(resultSet.getString("NOMBRES_PROVEEDOR"))
                                    .apellidos(resultSet.getString("APELLIDOS_PROVEEDOR"))
                                    .build())
                            .descripcion(resultSet.getString("DESCRIPCION"))
                            .precio(resultSet.getDouble("PRECIO"))
                            .precioMaximo(resultSet.getDouble("PRECIO_MAXIMO"))
                            .precioMinimo(resultSet.getDouble("PRECIO_MINIMO"))
                            .titulo(resultSet.getString("TITULO"))
                            .tipoPrecio(TipoPrecio.valueOf(resultSet.getString("TIPO_PRECIO")))
                            .ubicacion(resultSet.getString("UBICACION"))
                            .modalidad(Modalidad.valueOf(resultSet.getString("MODALIDAD")))
                            .aceptaTerminos(resultSet.getBoolean("ACEPTA_TERMINOS"))
                            .build());
                }

                if (!servicios.isEmpty()) {
                    return servicios;
                } else {
                    throw new ResourceNotFoundException("No existen los servicios indicados");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error durante la búsqueda de servicios");
        }
    }

    @Override
    public List<Servicio> obtenerByUsuario(UUID idUsuario)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("{CALL obtener_servicios_by_usuario(?)}")) {
            statement.setObject("p_id_usuario", UuidManager.UuidToBytes(idUsuario));

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Servicio> servicios = new ArrayList<>();

                while (resultSet.next()) {
                    servicios.add(Servicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .descripcion(resultSet.getString("DESCRIPCION"))
                            .precio(resultSet.getDouble("PRECIO"))
                            .precioMaximo(resultSet.getDouble("PRECIO_MAXIMO"))
                            .precioMinimo(resultSet.getDouble("PRECIO_MINIMO"))
                            .titulo(resultSet.getString("TITULO"))
                            .tipoPrecio(TipoPrecio.valueOf(resultSet.getString("TIPO_PRECIO")))
                            .ubicacion(resultSet.getString("UBICACION"))
                            .modalidad(Modalidad.valueOf(resultSet.getString("MODALIDAD")))
                            .aceptaTerminos(resultSet.getBoolean("ACEPTA_TERMINOS"))
                            .build());
                }

                if (!servicios.isEmpty()) {
                    return servicios;
                } else {
                    throw new ResourceNotFoundException("No se encontraron servicios del usuario");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error al buscar servicios");
        }
    }

    @Override
    public Servicio registrar(Servicio servicio) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL registrar_servicio(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
            statement.setBytes("p_id", UuidManager.UuidToBytes(servicio.getId()));
            statement.setString("p_titulo", servicio.getTitulo());
            statement.setString("p_descripcion", servicio.getDescripcion());
            statement.setDouble("p_precio", servicio.getPrecio());
            statement.setString("p_tipo_precio", servicio.getTipoPrecio().toString());
            statement.setDouble("p_precio_minimo", servicio.getPrecioMinimo());
            statement.setDouble("p_precio_maximo", servicio.getPrecioMaximo());
            statement.setString("p_ubicacion", servicio.getUbicacion());
            statement.setString("p_modalidad", servicio.getModalidad().toString());
            statement.setBoolean("p_acepta_terminos", servicio.isAceptaTerminos());
            statement.setBytes("p_id_proveedor", UuidManager.UuidToBytes(servicio.getProveedor().getId()));

            Servicio.ServicioBuilder servicioBuilder = Servicio.builder();
            List<ServicioDisponibilidad> disponibilidades = new ArrayList<>();
            List<RecursoMultimediaServicio> recursosMultimedia = new ArrayList<>();
            List<ServicioSkill> skills = new ArrayList<>();
            List<ModalidadPago> modalidadesPago = new ArrayList<>();

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    servicioBuilder
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .titulo(resultSet.getString("TITULO"))
                            .descripcion(resultSet.getString("DESCRIPCION"))
                            .precio(resultSet.getDouble("PRECIO"))
                            .tipoPrecio(TipoPrecio.valueOf(resultSet.getString("TIPO_PRECIO")))
                            .precioMinimo(resultSet.getDouble("PRECIO_MINIMO"))
                            .precioMaximo(resultSet.getDouble("PRECIO_MAXIMO"))
                            .ubicacion(resultSet.getString("UBICACION"))
                            .modalidad(Modalidad.valueOf(resultSet.getString("MODALIDAD")))
                            .aceptaTerminos(resultSet.getBoolean("ACEPTA_TERMINOS"))
                            .proveedor(servicio.getProveedor());
                }
            }

            // Registrar disponibilidades
            if (servicio.getDisponibilidades() != null) {
                for (ServicioDisponibilidad disp : servicio.getDisponibilidades()) {
                    try (CallableStatement dispStmt = connection
                            .prepareCall("{CALL registrar_servicio_disponibilidad(?, ?, ?, ?, ?)}")) {
                        dispStmt.setBytes("p_id", UuidManager.UuidToBytes(UuidManager.randomUuid()));
                        dispStmt.setBytes("p_id_servicio", UuidManager.UuidToBytes(servicio.getId()));
                        dispStmt.setString("p_dia", disp.getDia().toString());
                        dispStmt.setTime("p_hora_inicio", java.sql.Time.valueOf(disp.getHoraInicio()));
                        dispStmt.setTime("p_hora_fin", java.sql.Time.valueOf(disp.getHoraFin()));
                        try (ResultSet rs = dispStmt.executeQuery()) {
                            if (rs.next()) {
                                disponibilidades.add(ServicioDisponibilidad.builder()
                                        .id(UuidManager.bytesToUuid(rs.getBytes("ID")))
                                        .dia(Dia.valueOf(rs.getString("DIA")))
                                        .horaInicio(rs.getTime("HORA_INICIO").toLocalTime())
                                        .horaFin(rs.getTime("HORA_FIN").toLocalTime())
                                        .build());
                            }
                        }
                    }
                }
            }

            // Registrar recursos multimedia y obtenerlas
            if (servicio.getRecursosMultimediaServicio() != null) {
                for (RecursoMultimediaServicio img : servicio.getRecursosMultimediaServicio()) {
                    try (CallableStatement imgStmt = connection
                            .prepareCall("{CALL registrar_recurso_multimedia_servicio(?, ?, ?, ?)}")) {
                        imgStmt.setBytes("p_id", UuidManager.UuidToBytes(UuidManager.randomUuid()));
                        imgStmt.setBytes("p_id_servicio", UuidManager.UuidToBytes(servicio.getId()));
                        imgStmt.setString("p_url", img.getUrl());
                        imgStmt.setString("p_medio", img.getMedio().toString());
                        try (ResultSet rs = imgStmt.executeQuery()) {
                            if (rs.next()) {
                                recursosMultimedia.add(RecursoMultimediaServicio.builder()
                                        .id(UuidManager.bytesToUuid(rs.getBytes("ID")))
                                        .medio(Medio.valueOf(rs.getString("MEDIO")))
                                        .url(rs.getString("URL"))
                                        .build());
                            }
                        }
                    }
                }
            }

            // Registrar skills y obtenerlas
            if (servicio.getSkills() != null) {
                for (ServicioSkill skill : servicio.getSkills()) {
                    try (CallableStatement skillStmt = connection
                            .prepareCall("{CALL registrar_servicio_skill(?, ?)}")) {
                        skillStmt.setBytes("p_id_servicio", UuidManager.UuidToBytes(servicio.getId()));
                        skillStmt.setBytes("p_id_skill", UuidManager.UuidToBytes(skill.getSkill().getId()));
                        try (ResultSet rs = skillStmt.executeQuery()) {
                            if (rs.next()) {
                                skills.add(ServicioSkill.builder()
                                        .skill(Skill.builder()
                                                .id(UuidManager.bytesToUuid(rs.getBytes("ID_SKILL")))
                                                .build())
                                        .build());
                            }
                        }
                    }
                }
            }

            // Asignar modalidades de pago
            if (servicio.getModalidadesPago() != null) {
                for (ModalidadPago modalidad : servicio.getModalidadesPago()) {
                    try (CallableStatement mpStmt = connection
                            .prepareCall("{CALL registrar_modalidad_pago(?, ?, ?, ?, ?, ?)}")) {
                        mpStmt.setBytes("p_id", UuidManager.UuidToBytes(UuidManager.randomUuid()));
                        mpStmt.setBytes("p_id_servicio", UuidManager.UuidToBytes(servicio.getId()));
                        mpStmt.setString("p_tipo", modalidad.getTipo().toString());
                        mpStmt.setString("p_cuenta_bancaria", modalidad.getCuentaBancaria());
                        mpStmt.setString("p_numero_celular", modalidad.getNumeroCelular());
                        mpStmt.setString("p_url", modalidad.getUrl());
                        try (ResultSet rs = mpStmt.executeQuery()) {
                            if (rs.next()) {
                                modalidadesPago.add(ModalidadPago.builder()
                                        .id(UuidManager.bytesToUuid(rs.getBytes("ID")))
                                        .tipo(Tipo.valueOf(rs.getString("TIPO")))
                                        .cuentaBancaria(rs.getString("CUENTA_BANCARIA"))
                                        .numeroCelular(rs.getString("NUMERO_CELULAR"))
                                        .url(rs.getString("URL"))
                                        .build());
                            }
                        }
                    }
                }
            }

            servicioBuilder.disponibilidades(disponibilidades);
            servicioBuilder.recursosMultimediaServicio(recursosMultimedia);
            servicioBuilder.skills(skills);
            servicioBuilder.modalidadesPago(modalidadesPago);

            Servicio servicioRegistered = servicioBuilder.build();

            if (servicioRegistered != null) {
                return servicioRegistered;
            } else {
                throw new NotCreatedException("No se creó el servicio");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el servicio");
        }
    }
}
