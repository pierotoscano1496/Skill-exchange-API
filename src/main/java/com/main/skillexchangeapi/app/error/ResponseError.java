package com.main.skillexchangeapi.app.error;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
@Builder
public class ResponseError {
    private HttpStatusCode status;
    private String error;
    private String message;
}
