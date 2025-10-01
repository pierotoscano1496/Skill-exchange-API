package com.main.skillexchangeapi.infraestructure.repositories;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IServicioImagenRepository;
import com.main.skillexchangeapi.domain.entities.detail.ServicioImagen;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;

@Repository
public class ServicioImagenRepository implements IServicioImagenRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<ServicioImagen> registrarMultiple(List<ServicioImagen> servicioImagenes) throws DatabaseNotWorkingException, NotCreatedException {
        List<ServicioImagen> servicioImagenesRegistered = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_servicio_imagen(?, ?, ?)}")) {
            for (ServicioImagen servicioImagen : servicioImagenes) {
                try {
                    statement.setBytes("p_id", UuidManager.randomUuidToBytes());
                    statement.setBytes("p_id_servicio", UuidManager.UuidToBytes(servicioImagen.getServicio().getId()));
                    statement.setString("p_url_imagen", servicioImagen.getUrlImagen());

                    try (ResultSet resultSet = statement.executeQuery()) {
                        ServicioImagen servicioImagenRegistered = null;

                        while (resultSet.next()) {
                            servicioImagenRegistered = ServicioImagen.builder()
                                    .servicio(servicioImagen.getServicio())
                                    .urlImagen(resultSet.getString("URL_IMAGEN"))
                                    .build();

                            break;
                        }

                        if (servicioImagenRegistered != null) {
                            servicioImagenesRegistered.add(servicioImagenRegistered);
                        }
                    }
                } catch (SQLException e) {
                    servicioImagenesRegistered.clear();
                }
            }

            if (servicioImagenesRegistered.size() == servicioImagenes.size()) {
                return servicioImagenesRegistered;
            } else {
                throw new NotCreatedException("No se pudieron registrar todas las imágenes del servicio.");
            }
        } catch (Exception e) {
            throw new DatabaseNotWorkingException("No se pudieron registrar todas las imágenes del servicio.");
        }
    }

    

}
