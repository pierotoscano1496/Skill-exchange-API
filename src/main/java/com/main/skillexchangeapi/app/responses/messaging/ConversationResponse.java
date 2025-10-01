package com.main.skillexchangeapi.app.responses.messaging;

import com.main.skillexchangeapi.domain.entities.messaging.Contact;
import com.main.skillexchangeapi.domain.entities.messaging.Message;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ConversationResponse {
    private UUID conversationId;
    private Contact otherContact;
    private List<Message> messages;
}