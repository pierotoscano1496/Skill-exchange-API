package com.main.skillexchangeapi.domain.entities.messaging;

import java.util.List;

public interface MensajeChatProjection {
    List<Contact> getContacts();

    Message getLastMessage();
}
