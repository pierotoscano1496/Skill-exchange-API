package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.domain.abstractions.repositories.IUsuarioRepository;
import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.entities.Plan;
import com.main.skillexchangeapi.domain.entities.Skill;
import com.main.skillexchangeapi.domain.entities.Usuario;
import com.main.skillexchangeapi.domain.entities.detail.SkillUsuario;
import com.main.skillexchangeapi.domain.entities.security.UsuarioPersonalInfo;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.EncryptionAlghorithmException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.domain.logical.UsuarioContentRegister;
import com.main.skillexchangeapi.domain.logical.UsuarioCredenciales;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

@Repository
public class UsuarioRepository implements IUsuarioRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario login(UsuarioCredenciales credenciales) throws DatabaseNotWorkingException, ResourceNotFoundException, EncryptionAlghorithmException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL login(?, ?)}");) {
            statement.setString("p_correo", credenciales.getEmail());
            statement.setString("p_clave", credenciales.getClave());

            ResultSet resultSet = statement.executeQuery();

            Usuario usuario = null;

            while (resultSet.next()) {
                usuario = new Usuario(resultSet.getLong("id"));
                usuario.setDni(resultSet.getString("dni"));
                usuario.setCarnetExtranjeria(resultSet.getString("carnet_extranjeria"));
                usuario.setTipoDocumento(resultSet.getString("tipo_documento"));
                usuario.setNombres(resultSet.getNString("nombres"));
                usuario.setApellidos(resultSet.getNString("apellidos"));
                usuario.setFechaNacimiento(resultSet.getDate("fecha_nacimiento").toLocalDate());
                usuario.setPerfilLinkedin(resultSet.getString("perfil_linkedin"));
                usuario.setPerfilFacebook(resultSet.getString("perfil_facebook"));
                usuario.setPerfilInstagram(resultSet.getString("perfil_instagram"));
                usuario.setPerfilTiktok(resultSet.getString("perfil_tiktok"));

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
                usuarioPersonalInfo = new UsuarioPersonalInfo();
                usuarioPersonalInfo.setCorreo(resultSet.getString("correo"));
                usuarioPersonalInfo.setNombres(resultSet.getNString("nombres"));
                usuarioPersonalInfo.setApellidos(resultSet.getNString("apellidos"));
                usuarioPersonalInfo.setClave(resultSet.getNString("clave"));

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
             CallableStatement statement = connection.prepareCall("{CALL registrar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");) {
            statement.setString("p_dni", usuario.getDni());
            statement.setString("p_carnet_extranjeria", usuario.getCarnetExtranjeria());
            statement.setString("p_tipo_documento", usuario.getTipoDocumento());
            statement.setString("p_correo", usuario.getCorreo());
            statement.setString("p_nombres", usuario.getNombres());
            statement.setString("p_apellidos", usuario.getApellidos());
            statement.setDate("p_fecha_nacimiento", java.sql.Date.valueOf(usuario.getFechaNacimiento()));
            statement.setString("p_perfil_linkedin", usuario.getCarnetExtranjeria());
            statement.setString("p_perfil_facebook", usuario.getCarnetExtranjeria());
            statement.setString("p_perfil_instagram", usuario.getCarnetExtranjeria());
            statement.setString("p_perfil_tiktok", usuario.getCarnetExtranjeria());
            statement.setString("p_clave", passwordEncoder.encode(usuario.getClave()));
            statement.registerOutParameter("error_message", Types.VARCHAR);

            ResultSet resultSet = statement.getResultSet();

            String errorMessage = statement.getNString("error_message");
            if (errorMessage.isEmpty()) {
                Usuario usuarioRegistered = null;
                if (resultSet.first()) {
                    usuarioRegistered = new Usuario(resultSet.getLong("id"));
                    usuarioRegistered.setDni(resultSet.getString("dni"));
                    usuarioRegistered.setCarnetExtranjeria(resultSet.getString("carnet_extranjeria"));
                    usuarioRegistered.setTipoDocumento(resultSet.getString("tipo_documento"));
                    usuarioRegistered.setCorreo(resultSet.getString("correo"));
                    usuarioRegistered.setNombres(resultSet.getString("nombres"));
                    usuarioRegistered.setApellidos(resultSet.getString("apellidos"));
                    usuarioRegistered.setFechaNacimiento(resultSet.getDate("fecha_nacimiento").toLocalDate());
                    usuarioRegistered.setPerfilLinkedin(resultSet.getString("perfil_linkedin"));
                    usuarioRegistered.setPerfilFacebook(resultSet.getString("perfil_facebook"));
                    usuarioRegistered.setPerfilInstagram(resultSet.getString("perfil_instagram"));
                    usuarioRegistered.setPerfilTiktok(resultSet.getString("perfil_tiktok"));
                }
                resultSet.close();

                return usuarioRegistered;
            } else {
                throw new NotCreatedException(errorMessage);
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó el usuario");
        }
    }

    @Override
    public ArrayList<SkillUsuario> asignarSkills(Long id, ArrayList<SkillUsuario> skillsUsuario) throws DatabaseNotWorkingException, NotCreatedException {
        ArrayList<SkillUsuario> skillsUsuarioRegistered = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL asignar_skill_to_usuario(?, ?, ?)}");) {
            skillsUsuarioRegistered.forEach(skillUsuario -> {
                try {
                    statement.setLong("p_id_usuario", id);
                    statement.setLong("p_id_skill", skillUsuario.getSkill().getId());
                    statement.setInt("p_nivel_conocimiento", skillUsuario.getNivelConocimiento());

                    ResultSet resultSet = statement.getResultSet();

                    SkillUsuario skillUsuarioRegistered = null;

                    if (resultSet.first()) {
                        skillUsuarioRegistered = new SkillUsuario();
                        skillUsuarioRegistered.setIdSkill(resultSet.getLong("id_skill"));
                        skillUsuarioRegistered.setIdUsuario(resultSet.getLong("id_usuario"));
                        skillUsuarioRegistered.setNivelConocimiento(resultSet.getInt("nivel_conocimiento"));
                    }

                    resultSet.close();

                    if (skillUsuarioRegistered != null) {
                        skillsUsuarioRegistered.add(skillUsuarioRegistered);
                    } else {
                        throw new NotCreatedException("No se creó el skill");
                    }
                } catch (SQLException | NotCreatedException e) {
                    skillsUsuarioRegistered.clear();
                }
            });

            if (!skillsUsuarioRegistered.isEmpty()) {
                return skillsUsuarioRegistered;
            } else {
                throw new NotCreatedException("No se crearon los skills");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se crearon los skills");
        }
    }

    @Override
    public ArrayList<SkillUsuario> obtenerSkills(Long id) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL obtener_skills_from_usuario(?)}");) {
            statement.setLong("p_id_usuario", id);

            ResultSet resultSet = statement.executeQuery();
            ArrayList<SkillUsuario> skillsUsuario = new ArrayList<>();

            while (resultSet.next()) {
                SkillUsuario skillUsuario = new SkillUsuario();
                skillUsuario.setNivelConocimiento(resultSet.getInt("nivel_conocimiento"));

                Categoria categoria = new Categoria(resultSet.getLong("id_categoria"));
                categoria.setNombre(resultSet.getString("nombre_categoria"));

                Skill skill = new Skill(resultSet.getLong("id"));
                skill.setNombre("nombre");
                skill.setCategoria(categoria);

                skillUsuario.setSkill(skill);

                skillsUsuario.add(skillUsuario);
            }

            resultSet.close();

            if (!skillsUsuario.isEmpty()) {
                return skillsUsuario;
            } else {
                throw new ResourceNotFoundException("No hay planes");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se pudieron buscar los planes");
        }
    }
}
