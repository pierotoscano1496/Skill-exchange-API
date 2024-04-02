package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.PlanUsuario;
import com.main.skillexchangeapi.app.security.entities.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.logical.UsuarioCredenciales;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.sql.*;
import java.util.UUID;

@Repository
public class UsuarioRepository implements IUsuarioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public Usuario login(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, ResourceNotFoundException, EncryptionAlghorithmException {
        try (Connection connection = databaseConnection.getConnection(); CallableStatement statement = connection.prepareCall("{CALL login(?, ?)}");) {
            statement.setString("p_correo", credenciales.getEmail());
            statement.setString("p_clave", credenciales.getClave());

            try (ResultSet resultSet = statement.executeQuery()) {
                Usuario usuario = null;

                while (resultSet.next()) {
                    usuario = Usuario.builder()
                            .id(UUID.fromString(resultSet.getString("ID")))
                            .dni(resultSet.getString("DNI"))
                            .correo(resultSet.getString("CORREO"))
                            .carnetExtranjeria(resultSet.getString("CARNET_EXTRANJERIA"))
                            .tipoDocumento(resultSet.getString("TIPO_DOCUMENTO"))
                            .nombres(resultSet.getNString("NOMBRES"))
                            .apellidos(resultSet.getNString("APELLIDOS"))
                            .fechaNacimiento(resultSet.getDate("FECHA_NACIMIENTO").toLocalDate())
                            .perfilLinkedin(resultSet.getString("PERFIL_LINKEDIN"))
                            .perfilFacebook(resultSet.getString("PERFIL_FACEBOOK"))
                            .perfilInstagram(resultSet.getString("PERFIL_INSTAGRAM"))
                            .perfilTiktok(resultSet.getString("PERFIL_TIKTOK"))
                            .build();

                    break;
                }

                if (usuario != null) {
                    return usuario;
                } else {
                    throw new ResourceNotFoundException("Usuario no existe");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda del usuario");
        }
    }

    @Override
    public Usuario obtenerByCorreo(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection(); CallableStatement statement = connection.prepareCall("{CALL get_usuario_by_correo(?)}");) {
            statement.setString("p_correo", correo);

            try (ResultSet resultSet = statement.executeQuery()) {
                Usuario usuario = null;

                while (resultSet.next()) {
                    usuario = Usuario.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .nombres(resultSet.getNString("NOMBRES"))
                            .apellidos(resultSet.getNString("APELLIDOS"))
                            .dni(resultSet.getString("DNI"))
                            .carnetExtranjeria(resultSet.getString("CARNET_EXTRANJERIA"))
                            .tipoDocumento(resultSet.getString("TIPO_DOCUMENTO"))
                            .tipo(resultSet.getString("TIPO"))
                            .correo(resultSet.getString("CORREO"))
                            .fechaNacimiento(resultSet.getDate("FECHA_NACIMIENTO").toLocalDate())
                            .introduccion(resultSet.getString("INTRODUCCION"))
                            .perfilFacebook(resultSet.getString("PERFIL_FACEBOOK"))
                            .perfilInstagram(resultSet.getString("PERFIL_INSTAGRAM"))
                            .perfilLinkedin(resultSet.getString("PERFIL_LINKEDIN"))
                            .perfilTiktok(resultSet.getString("PERFIL_TIKTOK"))
                            .build();

                    break;
                }

                if (usuario != null) {
                    return usuario;
                } else {
                    throw new ResourceNotFoundException("Usuario no existe");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda del usuario");
        }
    }

    @Override
    public UsuarioPersonalInfo getUserCred(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection(); CallableStatement statement = connection.prepareCall("{CALL get_usercred_by_correo(?)}");) {
            statement.setString("p_correo", correo);

            try (ResultSet resultSet = statement.executeQuery()) {
                UsuarioPersonalInfo usuarioPersonalInfo = null;

                while (resultSet.next()) {
                    usuarioPersonalInfo = UsuarioPersonalInfo.builder()
                            .correo(resultSet.getString("CORREO"))
                            .nombres(resultSet.getNString("NOMBRES"))
                            .apellidos(resultSet.getNString("APELLIDOS"))
                            .clave(resultSet.getNString("CLAVE"))
                            .build();

                    break;
                }

                if (usuarioPersonalInfo != null) {
                    return usuarioPersonalInfo;
                } else {
                    throw new ResourceNotFoundException("Usuario no existe");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda del usuario");
        }
    }

    @Override
    public Usuario registrar(Usuario usuario) throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException {
        try (Connection connection = databaseConnection.getConnection(); CallableStatement statement = connection.prepareCall("{CALL registrar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");) {
            byte[] idUsuarioToBytes = UuidManager.randomUuidToBytes();
            statement.setObject("p_id", idUsuarioToBytes);
            statement.setString("p_dni", usuario.getDni());
            statement.setString("p_carnet_extranjeria", usuario.getCarnetExtranjeria());
            statement.setString("p_tipo_documento", usuario.getTipoDocumento());
            statement.setString("p_tipo", usuario.getTipo());
            statement.setString("p_introduccion", usuario.getIntroduccion());
            statement.setString("p_correo", usuario.getCorreo());
            statement.setString("p_nombres", usuario.getNombres());
            statement.setString("p_apellidos", usuario.getApellidos());
            statement.setDate("p_fecha_nacimiento", java.sql.Date.valueOf(usuario.getFechaNacimiento()));
            statement.setString("p_perfil_linkedin", usuario.getPerfilLinkedin());
            statement.setString("p_perfil_facebook", usuario.getPerfilFacebook());
            statement.setString("p_perfil_instagram", usuario.getPerfilInstagram());
            statement.setString("p_perfil_tiktok", usuario.getPerfilTiktok());
            statement.setString("p_clave", usuario.getClave());
            statement.registerOutParameter("status_message", Types.VARCHAR);

            statement.execute();

            try (ResultSet resultSet = statement.getResultSet()) {
                String statusMessage = statement.getString("status_message");
                if (statusMessage.equals("Usuario registrado")) {
                    Usuario usuarioRegistered = null;
                    while (resultSet.next()) {
                        UUID uuid = UuidManager.bytesToUuid(resultSet.getBytes("ID"));

                        usuarioRegistered = Usuario.builder()
                                .id(uuid)
                                .dni(resultSet.getString("DNI"))
                                .carnetExtranjeria(resultSet.getString("CARNET_EXTRANJERIA"))
                                .tipoDocumento(resultSet.getString("TIPO_DOCUMENTO"))
                                .tipo("TIPO")
                                .introduccion("INTRODUCCION")
                                .correo(resultSet.getString("CORREO"))
                                .nombres(resultSet.getString("NOMBRES"))
                                .apellidos(resultSet.getString("APELLIDOS"))
                                .fechaNacimiento(resultSet.getDate("FECHA_NACIMIENTO").toLocalDate())
                                .perfilLinkedin(resultSet.getString("PERFIL_LINKEDIN"))
                                .perfilFacebook(resultSet.getString("PERFIL_FACEBOOK"))
                                .perfilInstagram(resultSet.getString("PERFIL_INSTAGRAM"))
                                .perfilTiktok(resultSet.getString("PERFIL_TIKTOK"))
                                .build();
                        break;
                    }

                    return usuarioRegistered;
                } else {
                    throw new NotCreatedException(statusMessage);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el usuario");
        }
    }

    @Override
    public PlanUsuario asignarPlan(PlanUsuario planUsuario) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection(); CallableStatement statement = connection.prepareCall("{CALL registrar_plan_usuario(?, ?, ?, ?, ?)}")) {
            statement.setBytes("p_id_plan", UuidManager.UuidToBytes(planUsuario.getPlan().getId()));
            statement.setBytes("p_id_usuario", UuidManager.UuidToBytes(planUsuario.getUsuario().getId()));
            statement.setBoolean("p_is_active", planUsuario.isActive());
            statement.setDouble("p_monto", planUsuario.getMonto());
            statement.setString("p_moneda", planUsuario.getMoneda());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Plan plan = Plan.builder()
                            .id(UUID.fromString(resultSet.getString("ID_PLAN")))
                            .build();

                    PlanUsuario planUsuarioRegistered = PlanUsuario.builder()
                            .plan(plan)
                            .isActive(resultSet.getBoolean("IS_ACTIVE"))
                            .moneda(resultSet.getString("MONEDA"))
                            .monto(resultSet.getDouble("MONTO"))
                            .build();

                    return planUsuarioRegistered;
                } else {
                    throw new NotCreatedException("Error durante la asignación del plan");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteAll() throws DatabaseNotWorkingException {
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            return statement.executeUpdate("DELETE FROM USUARIO");
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error al eliminar los usuarios");
        }
    }
}
