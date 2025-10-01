package com.main.skillexchangeapi.domain.entities.messaging;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Conversation {
    private UUID id;
    private UUID idEmisor;
    private UUID idReceptor;
}
