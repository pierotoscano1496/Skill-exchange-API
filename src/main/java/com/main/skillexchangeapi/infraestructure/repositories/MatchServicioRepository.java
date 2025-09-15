package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.constants.MatchServicioConstants.Estado;
import com.main.skillexchangeapi.app.constants.ServicioConstants.TipoPrecio;
import com.main.skillexchangeapi.app.constants.UsuarioConstants.TipoDocumento;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchServicioRepository;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MatchServicioRepository implements IMatchServicioRepository {
    private enum MatchServicioFromProveedor {
        BY_OPTIONAL_ESTADO(1),
        IN_SERVING(2);

        private final int valor;

        MatchServicioFromProveedor(int valor) {
            this.valor = valor;
        }

        public int getValor() {
            return valor;
        }
    }

    @Autowired
    private DatabaseConnection databaseConnection;

    Logger logger = LoggerFactory.getLogger(MatchServicioRepository.class);

    @Override
    public List<MatchServicio> obtenerDetailsFromCliente(UUID idCliente)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL matchs_servicio_details_from_cliente(?)}")) {
            statement.setObject("p_id_cliente", UuidManager.UuidToBytes(idCliente));

            try (ResultSet resultSet = statement.executeQuery()) {
                List<MatchServicio> matchServicios = new ArrayList<>();

                while (resultSet.next()) {
                    Timestamp fechaInicio = resultSet.getTimestamp("FECHA_INICIO");
                    if (resultSet.wasNull()) {
                        fechaInicio = null;
                    }

                    Timestamp fechaCierre = resultSet.getTimestamp("FECHA_CIERRE");
                    if (resultSet.wasNull()) {
                        fechaCierre = null;
                    }

                    matchServicios.add(MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getTimestamp("FECHA").toLocalDateTime())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDateTime() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDateTime() : null)
                            .estado(Estado.valueOf(resultSet.getString("ESTADO")))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .descripcion(resultSet.getString("DESCRIPCION_SERVICIO"))
                                    .precio(resultSet.getDouble("PRECIO_SERVICIO"))
                                    .titulo(resultSet.getString("TITULO_SERVICIO"))
                                    .proveedor(Usuario.builder()
                                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_PROVEEDOR")))
                                            .apellidos(resultSet.getString("APELLIDOS_PROVEEDOR"))
                                            .carnetExtranjeria(resultSet.getString("CARNET_EXTRANJERIA_PROVEEDOR"))
                                            .correo(resultSet.getString("CORREO_PROVEEDOR"))
                                            .dni(resultSet.getString("DNI_PROVEEDOR"))
                                            .nombres(resultSet.getString("NOMBRES_PROVEEDOR"))
                                            .perfilFacebook(resultSet.getString("PERFIL_FACEBOOK_PROVEEDOR"))
                                            .perfilInstagram(resultSet.getString("PERFIL_INSTAGRAM_PROVEEDOR"))
                                            .perfilLinkedin(resultSet.getString("PERFIL_LINKEDIN_PROVEEDOR"))
                                            .perfilTiktok(resultSet.getString("PERFIL_TIKTOK_PROVEEDOR"))
                                            .tipoDocumento(TipoDocumento
                                                    .valueOf(resultSet.getString("TIPO_DOCUMENTO_PROVEEDOR")))
                                            .build())
                                    .build())
                            .cliente(Usuario.builder()
                                    .id(idCliente)
                                    .build())
                            .build());
                }

                if (!matchServicios.isEmpty()) {
                    return matchServicios;
                } else {
                    throw new ResourceNotFoundException("No se encontró el match");
                }
            }
        } catch (SQLException e) {
            logger.error("Error obtenerDetailsFromCliente::", e);
            throw new DatabaseNotWorkingException("Error de búsqueda del match");
        }
    }

    @Override
    public List<MatchServicio> obtenerDetailsFromProveedorAndOptionalEstado(UUID idProveedor, Estado estado)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        return obtenerDetailsFromProveedor(idProveedor, estado, MatchServicioFromProveedor.BY_OPTIONAL_ESTADO);
    }

    @Override
    public List<MatchServicio> obtenerDetailsFromProveedorInServing(UUID idProveedor)
            throws DatabaseNotWorkingException, ResourceNotFoundException {
        return obtenerDetailsFromProveedor(idProveedor, null, MatchServicioFromProveedor.IN_SERVING);
    }

    public List<MatchServicio> obtenerDetailsFromProveedor(UUID idProveedor, Estado estado,
            MatchServicioFromProveedor match) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL matchs_servicio_details_from_proveedor(?, ?, ?)}")) {
            statement.setObject("p_id_proveedor", UuidManager.UuidToBytes(idProveedor));
            statement.setString("p_estado", estado != null ? estado.toString() : null);
            statement.setInt("p_type_query", match.getValor());

            try (ResultSet resultSet = statement.executeQuery()) {
                List<MatchServicio> matchServicios = new ArrayList<>();

                while (resultSet.next()) {
                    Timestamp fechaInicio = resultSet.getTimestamp("FECHA_INICIO");
                    if (resultSet.wasNull()) {
                        fechaInicio = null;
                    }

                    Timestamp fechaCierre = resultSet.getTimestamp("FECHA_CIERRE");
                    if (resultSet.wasNull()) {
                        fechaCierre = null;
                    }

                    matchServicios.add(MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getTimestamp("FECHA").toLocalDateTime())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDateTime() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDateTime() : null)
                            .estado(Estado.valueOf(resultSet.getString("ESTADO")))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .mensaje(resultSet.getString("MENSAJE"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .descripcion(resultSet.getString("DESCRIPCION_SERVICIO"))
                                    .precio(resultSet.getDouble("PRECIO_SERVICIO"))
                                    .titulo(resultSet.getString("TITULO_SERVICIO"))
                                    .tipoPrecio(TipoPrecio.valueOf(resultSet.getString("TIPO_PRECIO_SERVICIO")))
                                    .proveedor(Usuario.builder()
                                            .id(idProveedor)
                                            .build())
                                    .build())
                            .cliente(Usuario.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_CLIENTE")))
                                    .apellidos(resultSet.getString("APELLIDOS_CLIENTE"))
                                    .carnetExtranjeria(resultSet.getString("CARNET_EXTRANJERIA_CLIENTE"))
                                    .correo(resultSet.getString("CORREO_CLIENTE"))
                                    .dni(resultSet.getString("DNI_CLIENTE"))
                                    .nombres(resultSet.getString("NOMBRES_CLIENTE"))
                                    .perfilFacebook(resultSet.getString("PERFIL_FACEBOOK_CLIENTE"))
                                    .perfilInstagram(resultSet.getString("PERFIL_INSTAGRAM_CLIENTE"))
                                    .perfilLinkedin(resultSet.getString("PERFIL_LINKEDIN_CLIENTE"))
                                    .perfilTiktok(resultSet.getString("PERFIL_TIKTOK_CLIENTE"))
                                    .tipoDocumento(
                                            TipoDocumento.valueOf(resultSet.getString("TIPO_DOCUMENTO_CLIENTE")))
                                    .build())
                            .build());
                }

                if (!matchServicios.isEmpty()) {
                    return matchServicios;
                } else {
                    throw new ResourceNotFoundException("No se encontró el match");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda del match");
        }
    }

    @Override
    public MatchServicio obtener(UUID id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("{CALL obtener_match_servicio(?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(id));

            try (ResultSet resultSet = statement.executeQuery()) {
                MatchServicio matchServicio = null;

                while (resultSet.next()) {
                    Timestamp fechaInicio = resultSet.getTimestamp("FECHA_INICIO");
                    if (resultSet.wasNull()) {
                        fechaInicio = null;
                    }

                    Timestamp fechaCierre = resultSet.getTimestamp("FECHA_CIERRE");
                    if (resultSet.wasNull()) {
                        fechaCierre = null;
                    }

                    matchServicio = MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getTimestamp("FECHA").toLocalDateTime())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDateTime() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDateTime() : null)
                            .estado(Estado.valueOf(resultSet.getString("ESTADO")))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .mensaje(resultSet.getString("MENSAJE"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .build())
                            .cliente(Usuario.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_CLIENTE")))
                                    .build())
                            .build();

                    break;
                }

                if (matchServicio != null) {
                    return matchServicio;
                } else {
                    throw new ResourceNotFoundException("No se encontraron matchs");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda de matchs");
        }
    }

    @Override
    public List<MatchServicio> obtenerByServicioAndCliente(UUID idServicio, UUID idCliente)
            throws ResourceNotFoundException, DatabaseNotWorkingException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL obtener_match_servicio_by_cliente_servicio(?, ?)}")) {
            statement.setObject("p_id_cliente", UuidManager.UuidToBytes(idCliente));
            statement.setObject("p_id_servicio", UuidManager.UuidToBytes(idServicio));

            try (ResultSet resultSet = statement.executeQuery()) {
                List<MatchServicio> matchServicios = new ArrayList<>();

                while (resultSet.next()) {
                    Timestamp fechaInicio = resultSet.getTimestamp("FECHA_INICIO");
                    if (resultSet.wasNull()) {
                        fechaInicio = null;
                    }

                    Timestamp fechaCierre = resultSet.getTimestamp("FECHA_CIERRE");
                    if (resultSet.wasNull()) {
                        fechaCierre = null;
                    }

                    matchServicios.add(MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getTimestamp("FECHA").toLocalDateTime())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDateTime() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDateTime() : null)
                            .estado(Estado.valueOf(resultSet.getString("ESTADO")))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .mensaje(resultSet.getString("MENSAJE"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .build())
                            .cliente(Usuario.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_CLIENTE")))
                                    .build())
                            .build());
                }

                if (!matchServicios.isEmpty()) {
                    return matchServicios;
                } else {
                    throw new ResourceNotFoundException("No se encontraron matchs");
                }
            }
        } catch (SQLException e) {
            logger.error("Error en búsqueda de matchs {}", e);
            throw new DatabaseNotWorkingException("Error de búsqueda de matchs");
        }
    }

    @Override
    public MatchServicio registrar(MatchServicio match) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL registrar_match_servicio (?, ?, ?, ?, ?, ?, ?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(match.getId()));
            statement.setTimestamp("p_fecha", Timestamp.valueOf(match.getFecha()));
            statement.setTimestamp("p_fecha_inicio",
                    match.getFechaInicio() != null ? Timestamp.valueOf(match.getFechaInicio()) : null);
            statement.setTimestamp("p_fecha_cierre",
                    match.getFechaCierre() != null ? Timestamp.valueOf(match.getFechaCierre()) : null);
            statement.setDouble("p_costo", match.getCosto());
            statement.setObject("p_id_servicio", UuidManager.UuidToBytes(match.getServicio().getId()));
            statement.setObject("p_id_cliente", UuidManager.UuidToBytes(match.getCliente().getId()));
            statement.setString("p_mensaje", match.getMensaje());

            try (ResultSet resultSet = statement.executeQuery()) {
                MatchServicio matchRegistered = null;

                while (resultSet.next()) {
                    matchRegistered = MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getTimestamp("FECHA").toLocalDateTime())
                            .fechaInicio(null)
                            .fechaCierre(null)
                            .estado(Estado.valueOf(resultSet.getString("ESTADO")))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .servicio(match.getServicio())
                            .cliente(match.getCliente())
                            .mensaje(resultSet.getString("MENSAJE"))
                            .build();

                    break;
                }

                if (matchRegistered != null) {
                    return matchRegistered;
                } else {
                    throw new NotCreatedException("No se creó el match");
                }
            }
        } catch (SQLException e) {
            logger.error("No se creó el match {}", e);
            throw new DatabaseNotWorkingException("No se creó el match");
        }
    }

    @Override
    public MatchServicio aceptar(UUID id, LocalDateTime fechaInicio)
            throws DatabaseNotWorkingException, NotUpdatedException {
        return actualizarProgreso(id, Estado.pendiente_pago, fechaInicio);
    }

    @Override
    public MatchServicio actualizarEstado(UUID id, Estado estado)
            throws DatabaseNotWorkingException, NotUpdatedException {
        return actualizarProgreso(id, estado, null);
    }

    private MatchServicio actualizarProgreso(UUID id, Estado estado, LocalDateTime fechaInicioValue)
            throws DatabaseNotWorkingException, NotUpdatedException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL actualizar_match_servicio_estado (?, ?, ?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(id));
            statement.setString("p_estado", estado.toString());
            statement.setTimestamp("p_fecha_inicio", Timestamp.valueOf(fechaInicioValue));
            statement.setTimestamp("p_fecha_cierre", switch (estado) {
                case finalizado, rechazado -> Timestamp.valueOf(LocalDateTime.now());
                default -> null;
            });

            try (ResultSet resultSet = statement.executeQuery()) {
                MatchServicio matchServicioUpdated = null;

                while (resultSet.next()) {
                    Timestamp fechaInicio = resultSet.getTimestamp("FECHA_INICIO");
                    if (resultSet.wasNull()) {
                        fechaInicio = null;
                    }

                    Timestamp fechaCierre = resultSet.getTimestamp("FECHA_CIERRE");
                    if (resultSet.wasNull()) {
                        fechaCierre = null;
                    }

                    matchServicioUpdated = MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getTimestamp("FECHA").toLocalDateTime())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDateTime() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDateTime() : null)
                            .estado(Estado.valueOf(resultSet.getString("ESTADO")))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .mensaje(resultSet.getString("MENSAJE"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .build())
                            .cliente(Usuario.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_CLIENTE")))
                                    .build())
                            .build();

                    break;
                }

                if (matchServicioUpdated != null) {
                    return matchServicioUpdated;
                } else {
                    throw new NotUpdatedException("No se actualizó el estado del match");
                }
            }
        } catch (SQLException e) {
            logger.error("No se actualizó el estado del match", e);
            throw new DatabaseNotWorkingException("No se actualizó el estado del match");
        }
    }

    @Override
    public MatchServicio puntuarServicio(UUID id, int puntuacion)
            throws DatabaseNotWorkingException, NotUpdatedException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall("{CALL puntuar_servicio (?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(id));
            statement.setInt("p_puntuacion", puntuacion);

            try (ResultSet resultSet = statement.executeQuery()) {
                MatchServicio matchServicioUpdated = null;

                Timestamp fechaInicio = resultSet.getTimestamp("FECHA_INICIO");
                if (resultSet.wasNull()) {
                    fechaInicio = null;
                }

                Timestamp fechaCierre = resultSet.getTimestamp("FECHA_CIERRE");
                if (resultSet.wasNull()) {
                    fechaCierre = null;
                }

                while (resultSet.next()) {
                    matchServicioUpdated = MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getTimestamp("FECHA").toLocalDateTime())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDateTime() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDateTime() : null)
                            .estado(Estado.valueOf(resultSet.getString("ESTADO")))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .mensaje(resultSet.getString("MENSAJE"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .build())
                            .cliente(Usuario.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_CLIENTE")))
                                    .build())
                            .build();

                    break;
                }

                if (matchServicioUpdated != null) {
                    return matchServicioUpdated;
                } else {
                    throw new NotUpdatedException("No se logró puntuar el match");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se logró puntuar el match");
        }
    }
}
