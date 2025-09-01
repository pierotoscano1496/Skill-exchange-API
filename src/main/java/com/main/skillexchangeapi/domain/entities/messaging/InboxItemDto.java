package com.main.skillexchangeapi.domain.entities.messaging;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboxItemDto {
    private UUID id;
    private Contact contact;
    private Message lastMessage;
    private Date lastDate;
}
