package com.main.skillexchangeapi.application.services.storage;

import com.main.skillexchangeapi.domain.abstractions.services.storage.IFirebaseFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FirebaseFileService implements IFirebaseFileService {
    @Value("${}")
}
