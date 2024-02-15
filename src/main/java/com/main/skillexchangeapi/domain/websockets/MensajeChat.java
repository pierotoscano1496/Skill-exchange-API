package com.main.skillexchangeapi.domain.websockets;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MensajeChat {
    private String message;
    private String user;
}
