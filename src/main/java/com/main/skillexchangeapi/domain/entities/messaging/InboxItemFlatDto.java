package com.main.skillexchangeapi.domain.entities.messaging;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboxItemFlatDto {
    private UUID id;
    private UUID contactId;
    private String contactFullName;
    private String contactEmail;
    private Message lastMessage;
    private Date lastDate;
}
