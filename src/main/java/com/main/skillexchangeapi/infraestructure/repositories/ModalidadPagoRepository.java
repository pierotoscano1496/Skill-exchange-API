package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IModalidadPagoRepository;
import com.main.skillexchangeapi.domain.entities.ModalidadPago;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ModalidadPagoRepository implements IModalidadPagoRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public ModalidadPago registrar(ModalidadPago modalidadPago) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_modalidad_pago(?, ?, ?, ?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(modalidadPago.getId()));
            statement.setString("p_tipo", modalidadPago.getTipo());
            statement.setString("p_cuenta_bancaria", modalidadPago.getCuentaBancaria());
            statement.setString("p_numero_celular", modalidadPago.getNumeroCelular());
            statement.setObject("p_id_servicio", UuidManager.UuidToBytes(modalidadPago.getServicio().getId()));

            ResultSet resultSet = statement.getResultSet();

            ModalidadPago modalidadPagoRegistered = null;

            if (resultSet.first()) {
                modalidadPagoRegistered = ModalidadPago.builder()
                        .id(UUID.fromString(resultSet.getString("ID")))
                        .tipo(resultSet.getString("TIPO"))
                        .cuentaBancaria(resultSet.getString("CUENTA_BANCARIA"))
                        .numeroCelular(resultSet.getString("NUMERO_CELULAR"))
                        .servicio(Servicio.builder()
                                .id(UUID.fromString(resultSet.getString("ID_SERVICIO")))
                                .build())
                        .build();
            }

            resultSet.close();

            if (modalidadPagoRegistered != null) {
                return modalidadPago;
            } else {
                throw new NotCreatedException("No se creó la modalidad de pago");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó la modalidad de pago");
        }
    }

    @Override
    public List<ModalidadPago> registrarMultiple(List<ModalidadPago> modalidadesPago) throws DatabaseNotWorkingException, NotCreatedException {
        List<ModalidadPago> modalidadesPagoRegistered = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_modalidad_pago(?, ?, ?, ?, ?)}")) {

            for (ModalidadPago modalidadPago : modalidadesPago) {
                try {
                    statement.setObject("p_id", UuidManager.UuidToBytes(modalidadPago.getId()));
                    statement.setString("p_tipo", modalidadPago.getTipo());
                    statement.setString("p_cuenta_bancaria", modalidadPago.getCuentaBancaria());
                    statement.setString("p_numero_celular", modalidadPago.getNumeroCelular());
                    statement.setObject("p_id_servicio", UuidManager.UuidToBytes(modalidadPago.getServicio().getId()));

                    try (ResultSet resultSet = statement.executeQuery()) {
                        ModalidadPago modalidadPagoRegistered = null;

                        while (resultSet.next()) {
                            modalidadPagoRegistered = ModalidadPago.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                                    .tipo(resultSet.getString("TIPO"))
                                    .cuentaBancaria(resultSet.getString("CUENTA_BANCARIA"))
                                    .numeroCelular(resultSet.getString("NUMERO_CELULAR"))
                                    .servicio(Servicio.builder()
                                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                            .build())
                                    .build();

                            break;
                        }

                        if (modalidadPagoRegistered != null) {
                            modalidadesPagoRegistered.add(modalidadPagoRegistered);
                        }
                    }
                } catch (SQLException e) {
                    throw new DatabaseNotWorkingException(e.getMessage());
                }
            }

            if (modalidadesPagoRegistered.size() == modalidadesPago.size()) {
                return modalidadesPagoRegistered;
            } else {
                throw new NotCreatedException("No se crearon las modalidades de pago");
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se crearon las modalidades de pago");
        }
    }
}
