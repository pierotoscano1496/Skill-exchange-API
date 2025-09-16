package com.main.skillexchangeapi.app.requests.matchservicio;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;

@Data
public class AcceptMatchServicioBody {
    private LocalDateTime fechaInicio;
}
