package com.main.skillexchangeapi.app.responses.messaging;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ConversationResponse {
    private UUID id;
    private UUID idEmisor;
    private UUID idReceptor;
}
