package com.main.skillexchangeapi.domain.entities.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private UUID sentBy;
    private Date fecha;
    private String mensaje;
    private String resourceUrl;
}
