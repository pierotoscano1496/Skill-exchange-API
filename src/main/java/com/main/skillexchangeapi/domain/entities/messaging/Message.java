package com.main.skillexchangeapi.domain.entities.messaging;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Message {
    private UUID sentBy;
    private Date fecha;
    private String mensaje;
    private String resourceUrl;
}
