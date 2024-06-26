package com.main.skillexchangeapi.app.requests.messaging;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FirstMessageChatBody {
    private UUID idReceptor;
    private String mensaje;
    private String resourceUrl;
}
