package com.main.skillexchangeapi.domain.entities.messaging;

import com.mongodb.lang.NonNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Document
public class MensajeChat {
    @Id
    private UUID id;

    @NonNull
    private List<Contact> contacts;

    private List<Message> messages;
    /*
    @NonNull
    private UUID idEmisor;

    @NonNull
    private UUID idReceptor;

    @NonNull
    private String mensaje;

    private String resourceUrl;

    @NonNull
    private Date fecha;

     */
}
