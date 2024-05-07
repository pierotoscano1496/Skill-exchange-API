package com.main.skillexchangeapi.domain.entities.messaging;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Message {
    private Date fecha;
    private String mensaje;
    private String resourceUrl;
}
