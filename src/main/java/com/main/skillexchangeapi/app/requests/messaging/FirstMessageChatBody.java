package com.main.skillexchangeapi.app.requests.messaging;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class FirstMessageChatBody {
    private UUID idReceptor;
    private String mensaje;
    private String resourceUrl;
}
