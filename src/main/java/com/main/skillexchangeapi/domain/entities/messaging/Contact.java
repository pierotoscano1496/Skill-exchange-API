package com.main.skillexchangeapi.domain.entities.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private UUID idContact;
    private String fullName;
    private String email;
}
