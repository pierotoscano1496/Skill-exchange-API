package com.main.skillexchangeapi.app.requests.messaging;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageBody {
    private UUID sentBy;
    private String mensaje;
    private String resourceUrl;
}
