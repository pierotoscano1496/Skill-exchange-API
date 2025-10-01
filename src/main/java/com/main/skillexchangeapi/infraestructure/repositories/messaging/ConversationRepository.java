package com.main.skillexchangeapi.infraestructure.repositories.messaging;

import com.main.skillexchangeapi.app.utils.UuidManager;
import com.main.skillexchangeapi.domain.abstractions.repositories.messaging.IConversationRepository;
import com.main.skillexchangeapi.domain.entities.messaging.Conversation;
import com.main.skillexchangeapi.domain.exceptions.DatabaseNotWorkingException;
import com.main.skillexchangeapi.domain.exceptions.NotCreatedException;
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
public class ConversationRepository implements IConversationRepository {
    @Autowired
    private DatabaseConnection databaseConnection;

    @Override
    public Conversation registrar(Conversation conversation) throws DatabaseNotWorkingException, NotCreatedException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL registrar_conversation(?, ?, ?)}")) {
            statement.setObject("p_id", UuidManager.UuidToBytes(conversation.getId()));
            statement.setObject("p_id_emisor", UuidManager.UuidToBytes(conversation.getIdEmisor()));
            statement.setObject("p_id_receptor", UuidManager.UuidToBytes(conversation.getIdReceptor()));

            try (ResultSet resultSet = statement.executeQuery()) {
                Conversation conversationRegistered = null;

                while (resultSet.next()) {
                    conversationRegistered = Conversation.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .idEmisor(UuidManager.bytesToUuid(resultSet.getBytes("ID_EMISOR")))
                            .idReceptor(UuidManager.bytesToUuid(resultSet.getBytes("ID_RECEPTOR")))
                            .build();

                    break;
                }

                if (conversationRegistered != null) {
                    return conversationRegistered;
                } else {
                    throw new NotCreatedException("No se creó la conversación");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("No se creó la conversación");
        }
    }

    @Override
    public List<Conversation> obtenerFromUser(UUID idUsuario) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL obtener_conversations_from_user(?)}")) {
            statement.setObject("p_id_user", UuidManager.UuidToBytes(idUsuario));

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Conversation> conversations = new ArrayList<>();

                while (resultSet.next()) {
                    conversations.add(Conversation.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .idEmisor(UuidManager.bytesToUuid(resultSet.getBytes("ID_EMISOR")))
                            .idReceptor(UuidManager.bytesToUuid(resultSet.getBytes("ID_RECEPTOR")))
                            .build());
                }

                if (!conversations.isEmpty()) {
                    return conversations;
                } else {
                    throw new ResourceNotFoundException("No se encontraron conversaciones");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error en la búsqueda de conversaciones");
        }
    }

    @Override
    public Conversation obtenerFromUsers(UUID idUsuario1, UUID idUsuario2) throws DatabaseNotWorkingException, ResourceNotFoundException {
        try (Connection connection = databaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL obtener_unique_conversations_from_users(?, ?)}")) {
            statement.setObject("p_id_user1", UuidManager.UuidToBytes(idUsuario1));
            statement.setObject("p_id_user2", UuidManager.UuidToBytes(idUsuario2));

            try (ResultSet resultSet = statement.executeQuery()) {
                Conversation conversation = null;

                while (resultSet.next()) {
                    conversation = Conversation.builder()
                            .id(UuidManager.bytesToUuid(resultSet.getBytes("ID")))
                            .idEmisor(UuidManager.bytesToUuid(resultSet.getBytes("ID_EMISOR")))
                            .idReceptor(UuidManager.bytesToUuid(resultSet.getBytes("ID_RECEPTOR")))
                            .build();

                    break;
                }

                if (conversation != null) {
                    return conversation;
                } else {
                    throw new ResourceNotFoundException("No se encontraron conversaciones");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseNotWorkingException("Error en la búsqueda de conversaciones");
        }
    }
}
