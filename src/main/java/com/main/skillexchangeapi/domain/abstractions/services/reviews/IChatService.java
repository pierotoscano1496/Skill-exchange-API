package com.main.skillexchangeapi.domain.abstractions.services.reviews;

import com.main.skillexchangeapi.domain.entities.messaging.MensajeChat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IChatService {
    MensajeChat saveMensaje(MensajeChat mensajeChat);

    List<MensajeChat> obtenerConversacion(UUID idUsuario1, UUID idUsuario2);
}
