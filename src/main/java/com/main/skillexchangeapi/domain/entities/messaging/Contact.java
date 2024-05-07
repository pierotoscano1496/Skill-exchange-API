package com.main.skillexchangeapi.domain.entities.messaging;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Contact {
    private UUID idContact;
    private String fullName;
    private String email;
}
