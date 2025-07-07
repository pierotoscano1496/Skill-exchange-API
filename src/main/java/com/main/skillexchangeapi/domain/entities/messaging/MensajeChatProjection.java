package com.main.skillexchangeapi.domain.entities.messaging;

import java.util.List;
import java.util.UUID;

public interface MensajeChatProjection {
    UUID getId();
    List<Contact> getContacts();
    Message getLastMessage();
}
