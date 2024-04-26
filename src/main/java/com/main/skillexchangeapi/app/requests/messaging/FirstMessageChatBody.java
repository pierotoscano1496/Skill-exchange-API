package com.main.skillexchangeapi.app.requests.messaging;

import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
public class FirstMessageChatBody {
    private UUID idReceptor;
    private String mensaje;
    private String resourceUrl;
    private Date fecha = new Date();
}
