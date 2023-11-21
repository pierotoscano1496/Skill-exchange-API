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

import java.sql.*;
import java.util.UUID;

@Repository
public class UsuarioRepository implements IUsuarioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public Usuario login(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, ResourceNotFoundException, EncryptionAlghorithmException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL login(?, ?)}");) {
            statement.setString("p_correo", credenciales.getEmail());
            statement.setString("p_clave", credenciales.getClave());

            ResultSet resultSet = statement.executeQuery();

            Usuario usuario = null;

            while (resultSet.next()) {
                usuario = Usuario.builder()
                        .id(UUID.fromString(resultSet.getString("id")))
                        .dni(resultSet.getString("dni"))
                        .carnetExtranjeria(resultSet.getString("carnet_extranjeria"))
                        .tipoDocumento(resultSet.getString("tipo_documento"))
                        .nombres(resultSet.getNString("nombres"))
                        .apellidos(resultSet.getNString("apellidos"))
                        .fechaNacimiento(resultSet.getDate("fecha_nacimiento").toLocalDate())
                        .perfilLinkedin(resultSet.getString("perfil_linkedin"))
                        .perfilFacebook(resultSet.getString("perfil_facebook"))
                        .perfilInstagram(resultSet.getString("perfil_instagram"))
                        .perfilTiktok(resultSet.getString("perfil_tiktok"))
                        .build();

                break;
            }
            resultSet.close();

            if (usuario != null) {
                return usuario;
            } else {
                throw new ResourceNotFoundException("Usuario no existe");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda del usuario");
        }
    }

    @Override
    public UsuarioPersonalInfo getUserCred(String correo) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL get_usercred_by_correo(?)}");) {
            statement.setString("p_correo", correo);

            ResultSet resultSet = statement.executeQuery();

            UsuarioPersonalInfo usuarioPersonalInfo = null;

            while (resultSet.next()) {
                usuarioPersonalInfo = UsuarioPersonalInfo.builder()
                        .correo(resultSet.getString("correo"))
                        .nombres(resultSet.getNString("nombres"))
                        .apellidos(resultSet.getNString("apellidos"))
                        .clave(resultSet.getNString("clave"))
                        .build();

                break;
            }
            resultSet.close();

            if (usuarioPersonalInfo != null) {
                return usuarioPersonalInfo;
            } else {
                throw new ResourceNotFoundException("Usuario no existe");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda del usuario");
        }
    }

    @Override
    public Usuario registrar(Usuario usuario) throws DatabaseNotWorkingException, NotCreatedException, EncryptionAlghorithmException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");) {
            byte[] idUsuarioToBytes = UuidManager.randomUuidToBytes();
            statement.setObject("p_id", idUsuarioToBytes);
            statement.setString("p_dni", usuario.getDni());
            statement.setString("p_carnet_extranjeria", usuario.getCarnetExtranjeria());
            statement.setString("p_tipo_documento", usuario.getTipoDocumento());
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

            ResultSet resultSet = statement.getResultSet();

            String statusMessage = statement.getString("status_message");
            if (statusMessage.equals("Usuario registrado")) {
                Usuario usuarioRegistered = null;
                while (resultSet.next()) {
                    usuarioRegistered = Usuario.builder()
                            //.id(UUID.fromString(resultSet.getString("id")))
                            .dni(resultSet.getString("dni"))
                            .carnetExtranjeria(resultSet.getString("carnet_extranjeria"))
                            .tipoDocumento(resultSet.getString("tipo_documento"))
                            .correo(resultSet.getString("correo"))
                            .nombres(resultSet.getString("nombres"))
                            .apellidos(resultSet.getString("apellidos"))
                            .fechaNacimiento(resultSet.getDate("fecha_nacimiento").toLocalDate())
                            .perfilLinkedin(resultSet.getString("perfil_linkedin"))
                            .perfilFacebook(resultSet.getString("perfil_facebook"))
                            .perfilInstagram(resultSet.getString("perfil_instagram"))
                            .perfilTiktok(resultSet.getString("perfil_tiktok"))
                            .build();
                    break;
                }
                resultSet.close();

                return usuarioRegistered;
            } else {
                throw new NotCreatedException(statusMessage);
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el usuario");
        }
    }

    @Override
    public PlanUsuario asignarPlan(PlanUsuario planUsuario) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_plan_usuario(?, ?, ?, ?, ?)}")) {
            statement.setBytes("p_id_plan", UuidManager.UuidToBytes(planUsuario.getPlan().getId()));
            statement.setBytes("p_id_usuario", UuidManager.UuidToBytes(planUsuario.getUsuario().getId()));
            statement.setBoolean("p_is_active", planUsuario.isActive());
            statement.setDouble("p_monto", planUsuario.getMonto());
            statement.setString("p_moneda", planUsuario.getMoneda());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Plan plan = Plan.builder()
                        .id(UUID.fromString(resultSet.getString("id_plan")))
                        .build();

                PlanUsuario planUsuarioRegistered = PlanUsuario.builder()
                        .plan(plan)
                        .isActive(resultSet.getBoolean("is_active"))
                        .moneda(resultSet.getString("moneda"))
                        .monto(resultSet.getDouble("monto"))
                        .build();

                return planUsuarioRegistered;
            } else {
                throw new NotCreatedException("Error durante la asignación del plan");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
