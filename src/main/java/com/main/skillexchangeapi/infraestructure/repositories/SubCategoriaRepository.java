package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.ISubCategoriaRepository;
import com.main.skillexchangeapi.domain.entities.Categoria;
import com.main.skillexchangeapi.domain.entities.SubCategoria;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
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
public class SubCategoriaRepository implements ISubCategoriaRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<SubCategoria> obtenerByCategoria(UUID idCategoria) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("CALL obtener_sub_categorias_by_categoria(?)");) {
            statement.setBytes("p_id_categoria", UuidManager.UuidToBytes(idCategoria));

            ResultSet resultSet = statement.executeQuery();
            List<SubCategoria> subCategorias = new ArrayList<>();

            while (resultSet.next()) {
                subCategorias.add(SubCategoria.builder()
                        .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                        .categoria(Categoria.builder()
                                .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_CATEGORIA")))
                                .build())
                        .nombre(resultSet.getString("NOMBRE"))
                        .build());
            }

            resultSet.close();

            if (!subCategorias.isEmpty()) {
                return subCategorias;
            } else {
                throw new ResourceNotFoundException("No se encontraron sub categorías para la categoría indicada");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error de búsqueda de subcategorías");
        }
    }
}
