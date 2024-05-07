package com.main.skillexchangeapi.app.requests.messaging;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Builder
@Data
public class CreateConversationBody {
    private UUID idEmisor;
    private UUID idReceptor;
}
