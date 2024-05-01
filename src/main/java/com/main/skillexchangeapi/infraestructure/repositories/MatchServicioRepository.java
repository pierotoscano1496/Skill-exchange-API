package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IMatchServicioRepository;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.NotUpdatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MatchServicioRepository implements IMatchServicioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<MatchServicio> obtenerDetailsFromPrestamistaByOptionalEstado(UUID idPrestamista, String estado) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL matchs_servicio_details_from_prestamista_by_optional_estado(?, ?)}")) {
            statement.setObject("p_id_prestamista", UuidManager.UuidToBytes(idPrestamista));
            statement.setString("p_estado", estado);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<MatchServicio> matchServicios = new ArrayList<>();

                while (resultSet.next()) {
                    Date fechaInicio = resultSet.getDate("FECHA_INICIO");
                    if (resultSet.wasNull()) {
                        fechaInicio = null;
                    }

                    Date fechaCierre = resultSet.getDate("FECHA_CIERRE");
                    if (resultSet.wasNull()) {
                        fechaCierre = null;
                    }

                    matchServicios.add(MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getDate("FECHA").toLocalDate())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDate() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDate() : null)
                            .estado(resultSet.getString("ESTADO"))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .descripcion(resultSet.getString("DESCRIPCION_SERVICIO"))
                                    .precio(resultSet.getDouble("PRECIO_SERVICIO"))
                                    .titulo(resultSet.getString("TITULO_SERVICIO"))
                                    .skillUsuario(SkillUsuario.builder()
                                            .usuario(Usuario.builder()
                                                    .id(idPrestamista)
                                                    .build())
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
                                    .tipo(resultSet.getString("TIPO_CLIENTE"))
                                    .tipoDocumento(resultSet.getString("TIPO_DOCUMENTO_CLIENTE"))
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
                    Date fechaInicio = resultSet.getDate("FECHA_INICIO");
                    if (resultSet.wasNull()) {
                        fechaInicio = null;
                    }

                    Date fechaCierre = resultSet.getDate("FECHA_CIERRE");
                    if (resultSet.wasNull()) {
                        fechaCierre = null;
                    }

                    matchServicio = MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getDate("FECHA").toLocalDate())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDate() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDate() : null)
                            .estado(resultSet.getString("ESTADO"))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
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
    public MatchServicio registrar(MatchServicio match) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_match_servicio (?, ?, ?, ?, ?, ?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(match.getId()));
            statement.setDouble("p_costo", match.getCosto());
            statement.setObject("p_id_servicio", UuidManager.UuidToBytes(match.getServicio().getId()));
            statement.setObject("p_id_cliente", UuidManager.UuidToBytes(match.getCliente().getId()));

            try (ResultSet resultSet = statement.executeQuery()) {
                MatchServicio matchRegistered = null;

                while (resultSet.next()) {
                    matchRegistered = MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getDate("FECHA").toLocalDate())
                            .fechaInicio(null)
                            .fechaCierre(null)
                            .estado(resultSet.getString("ESTADO"))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
                            .servicio(match.getServicio())
                            .cliente(match.getCliente())
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
            throw new DatabaseNotWorkingException("No se creó el match");
        }
    }

    @Override
    public MatchServicio actualizarEstado(UUID id, String estado) throws DatabaseNotWorkingException, NotUpdatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL actualizar_match_servicio_estado (?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(id));
            statement.setString("p_estado", estado);

            try (ResultSet resultSet = statement.executeQuery()) {
                MatchServicio matchServicioUpdated = null;

                Date fechaInicio = resultSet.getDate("FECHA_INICIO");
                if (resultSet.wasNull()) {
                    fechaInicio = null;
                }

                Date fechaCierre = resultSet.getDate("FECHA_CIERRE");
                if (resultSet.wasNull()) {
                    fechaCierre = null;
                }

                while (resultSet.next()) {
                    matchServicioUpdated = MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getDate("FECHA").toLocalDate())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDate() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDate() : null)
                            .estado(resultSet.getString("ESTADO"))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
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
            throw new DatabaseNotWorkingException("No se actualizó el estado del match");
        }
    }

    @Override
    public MatchServicio puntuarServicio(UUID id, int puntuacion) throws DatabaseNotWorkingException, NotUpdatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL puntuar_servicio (?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(id));
            statement.setInt("p_puntuacion", puntuacion);

            try (ResultSet resultSet = statement.executeQuery()) {
                MatchServicio matchServicioUpdated = null;

                Date fechaInicio = resultSet.getDate("FECHA_INICIO");
                if (resultSet.wasNull()) {
                    fechaInicio = null;
                }

                Date fechaCierre = resultSet.getDate("FECHA_CIERRE");
                if (resultSet.wasNull()) {
                    fechaCierre = null;
                }

                while (resultSet.next()) {
                    matchServicioUpdated = MatchServicio.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .fecha(resultSet.getDate("FECHA").toLocalDate())
                            .fechaInicio(fechaInicio != null ? fechaInicio.toLocalDate() : null)
                            .fechaCierre(fechaCierre != null ? fechaCierre.toLocalDate() : null)
                            .estado(resultSet.getString("ESTADO"))
                            .puntuacion(resultSet.getInt("PUNTUACION"))
                            .costo(resultSet.getDouble("COSTO"))
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
