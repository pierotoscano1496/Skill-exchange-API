package com.main.skillexchangeapi.domain.entities.messaging;

import java.util.UUID;

import com.google.auto.value.AutoValue.Builder;

import lombok.Data;

@Data
@Builder
public class LastMessage {
    private UUID idConversation;
    private String lastMessage;
    private UUID sentBy;
}
