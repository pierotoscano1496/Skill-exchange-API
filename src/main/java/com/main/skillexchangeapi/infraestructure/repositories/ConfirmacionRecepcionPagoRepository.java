package com.main.skillexchangeapi.infraestructure.repositories;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;
import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.IConfirmacionRecepcionPagoRepository;
import com.main.skillexchangeapi.domain.entities.MatchServicio;
import com.main.skillexchangeapi.domain.entities.detail.ConfirmacionRecepcionPago;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
import com.main.skillexchangeapi.infraestructure.database.DatabaseConnection;

@Repository
public class ConfirmacionRecepcionPagoRepository implements IConfirmacionRecepcionPagoRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ConfirmacionRecepcionPago registrar(ConfirmacionRecepcionPago confirmacion)
            throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
                CallableStatement statement = connection.prepareCall(
                        "{CALL registrar_confirmacion_recepcion_pago_match_servicio(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
            logger.info("Id de match {}", confirmacion.getMatchServicio().getId());
            statement.setObject("p_id", UuidManager.UuidToBytes(UuidManager.randomUuid()));
            statement.setObject("p_id_match_servicio", UuidManager.UuidToBytes(
                    confirmacion.getMatchServicio().getId()));
            statement.setBoolean("p_pago_completo_acordado", confirmacion.isPagoCompletoAcordado());
            statement.setBoolean("p_metodo_pago_acordado", confirmacion.isMetodoPagoAcordado());
            statement.setBoolean("p_comprobante_recibido", confirmacion.isComprobanteRecibido());
            statement.setBoolean("p_monto_recibido_correcto", confirmacion.isMontoRecibidoCorrecto());
            statement.setString("p_metodo_pago_recibido", confirmacion.getMetodoPagoRecibido().toString());
            statement.setDouble("p_monto_recibido", confirmacion.getMontoRecibido());
            statement.setString("p_numero_comprobante", confirmacion.getNumeroComprobante());
            statement.setString("p_notas_adicionales", confirmacion.getNotasAdicionales());
            statement.setBoolean("p_confirmacion_ejecucion_servicio", confirmacion.isConfirmacionEjecucionServicio());

            try (ResultSet resultSet = statement.executeQuery()) {
                ConfirmacionRecepcionPago confirmacionRegistered = null;

                while (resultSet.next()) {
                    confirmacionRegistered = ConfirmacionRecepcionPago.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .matchServicio(MatchServicio.builder()
                                    .id(UuidManager.bytesToUuid(resultSet.getBytes("ID_MATCH_SERVICIO"))).build())
                            .pagoCompletoAcordado(resultSet.getBoolean("PAGO_COMPLETO_ACORDADO"))
                            .metodoPagoAcordado(resultSet.getBoolean("METODO_PAGO_ACORDADO"))
                            .comprobanteRecibido(resultSet.getBoolean("COMPROBANTE_RECIIBIDO"))
                            .montoRecibidoCorrecto(resultSet.getBoolean("MONTO_RECIBIDO_CORRECTO"))
                            .metodoPagoRecibido(
                                    ModalidadPagoConstants.Tipo.valueOf(resultSet.getString("METODO_PAGO_RECIBIDO")))
                            .montoRecibido(resultSet.getDouble("MONTO_RECIBIDO"))
                            .numeroComprobante(resultSet.getString("NUMERO_COMPROBANTE"))
                            .notasAdicionales(resultSet.getString("NOTAS_ADICIONALES"))
                            .confirmacionEjecucionServicio(resultSet.getBoolean("CONFIRMACION_EJECUCION_SERVICIO"))
                            .build();
                    break;
                }

                if (confirmacionRegistered != null) {
                    return confirmacionRegistered;
                } else {
                    throw new NotCreatedException("No se creó la confirmación");
                }
            }
        } catch (SQLException e) {
            logger.error("No se creó la confirmación {}", e);
            throw new DatabaseNotWorkingException("No se creó la confirmación");
        }
    }
}
