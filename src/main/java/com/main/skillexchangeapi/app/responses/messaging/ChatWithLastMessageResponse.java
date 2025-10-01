package com.main.skillexchangeapi.app.responses.messaging;

import com.main.skillexchangeapi.domain.entities.messaging.Contact;
import com.main.skillexchangeapi.domain.entities.messaging.Message;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ChatWithLastMessageResponse {
    private UUID conversationId;
    private Contact contact;
    private Message lastMessage;
}
