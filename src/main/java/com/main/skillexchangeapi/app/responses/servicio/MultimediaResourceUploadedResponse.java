package com.main.skillexchangeapi.app.responses.servicio;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultimediaResourceUploadedResponse {
    private String url;
    private String medio;
}
