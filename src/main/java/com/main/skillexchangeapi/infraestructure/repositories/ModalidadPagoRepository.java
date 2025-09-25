package com.main.skillexchangeapi.infraestructure.repositories;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants.Tipo;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IModalidadPagoRepository;
import com.main.skillexchangeapi.domain.entities.ModalidadPago;
import com.main.skillexchangeapi.domain.entities.Servicio;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.domain.exceptions.ResourceNotFoundException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ModalidadPagoRepository implements IModalidadPagoRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public List<ModalidadPago> obtenerByServicio(UUID idServicio)
            throws ResourceNotFoundException, DatabaseNotWorkingException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL obtener_modalidades_pago_by_servicio(?)}")) {
            statement.setObject("p_id_servicio", UuidManager.UuidToBytes(idServicio));

            try (ResultSet resultSet = statement.executeQuery()) {
                List<ModalidadPago> modalidadesPago = new ArrayList<>();

                while (resultSet.next()) {
                    modalidadesPago.add(ModalidadPago.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .tipo(Tipo.valueOf(resultSet.getString("TIPO")))
                            .cuentaBancaria(resultSet.getString("CUENTA_BANCARIA"))
                            .numeroCelular(resultSet.getString("NUMERO_CELULAR"))
                            .url(resultSet.getString("URL"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .build())
                            .build());
                }

                if (!modalidadesPago.isEmpty()) {
                    return modalidadesPago;
                } else {
                    throw new ResourceNotFoundException("No existen modalidades de pago para el servicio");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error durante la búsqueda de modalidades de pago");
        }
    }

    @Override
    public ModalidadPago obtener(UUID id) throws ResourceNotFoundException, DatabaseNotWorkingException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL obtener_modalidad_pago(?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(id));

            try (ResultSet resultSet = statement.executeQuery()) {
                ModalidadPago modalidadPago = null;

                while (resultSet.next()) {
                    modalidadPago = ModalidadPago.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .tipo(Tipo.valueOf(resultSet.getString("TIPO")))
                            .cuentaBancaria(resultSet.getString("CUENTA_BANCARIA"))
                            .numeroCelular(resultSet.getString("NUMERO_CELULAR"))
                            .url(resultSet.getString("URL"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .build())
                            .build();

                    break;
                }

                if (modalidadPago != null) {
                    return modalidadPago;
                } else {
                    throw new ResourceNotFoundException("No existe la modalidad de pago");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error durante la búsqueda de la modalidad de pago");
        }
    }

    @Override
    public ModalidadPago registrar(ModalidadPago modalidadPago)
            throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL registrar_modalidad_pago(?, ?, ?, ?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(modalidadPago.getId()));
            statement.setString("p_tipo", modalidadPago.getTipo().toString());
            statement.setString("p_cuenta_bancaria", modalidadPago.getCuentaBancaria());
            statement.setString("p_numero_celular", modalidadPago.getNumeroCelular());
            statement.setString("p_url", modalidadPago.getUrl());
            statement.setObject("p_id_servicio", UuidManager.UuidToBytes(modalidadPago.getServicio().getId()));

            try (ResultSet resultSet = statement.executeQuery()) {
                ModalidadPago modalidadPagoRegistered = null;

                while (resultSet.next()) {
                    modalidadPagoRegistered = ModalidadPago.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .tipo(Tipo.valueOf(resultSet.getString("TIPO")))
                            .cuentaBancaria(resultSet.getString("CUENTA_BANCARIA"))
                            .numeroCelular(resultSet.getString("NUMERO_CELULAR"))
                            .url(resultSet.getString("URL"))
                            .servicio(Servicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_SERVICIO")))
                                    .build())
                            .build();

                    break;
                }

                if (modalidadPagoRegistered != null) {
                    return modalidadPago;
                } else {
                    throw new NotCreatedException("No se creó la modalidad de pago");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó la modalidad de pago");
        }
    }

    @Override
    public List<ModalidadPago> registrarMultiple(List<ModalidadPago> modalidadesPago)
            throws DatabaseNotWorkingException, NotCreatedException {
        List<ModalidadPago> modalidadesPagoRegistered = new ArrayList<>();

        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection
                        .prepareCall("{CALL registrar_modalidad_pago(?, ?, ?, ?, ?, ?)}")) {

            for (ModalidadPago modalidadPago : modalidadesPago) {
                try {
                    byte[] idModalidadPagoToBytes = UuidManager.randomUuidToBytes();
                    statement.setObject("p_id", idModalidadPagoToBytes);
                    statement.setString("p_tipo", modalidadPago.getTipo().toString());
                    statement.setString("p_cuenta_bancaria", modalidadPago.getCuentaBancaria());
                    statement.setString("p_numero_celular", modalidadPago.getNumeroCelular());
                    statement.setString("p_url", modalidadPago.getUrl());
                    statement.setObject("p_id_servicio", UuidManager.UuidToBytes(modalidadPago.getServicio().getId()));

                    try (ResultSet resultSet = statement.executeQuery()) {
                        ModalidadPago modalidadPagoRegistered = null;

                        while (resultSet.next()) {
                            modalidadPagoRegistered = ModalidadPago.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                                    .tipo(Tipo.valueOf(resultSet.getString("TIPO")))
                                    .cuentaBancaria(resultSet.getString("CUENTA_BANCARIA"))
                                    .numeroCelular(resultSet.getString("NUMERO_CELULAR"))
                                    .url(resultSet.getString("URL"))
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
