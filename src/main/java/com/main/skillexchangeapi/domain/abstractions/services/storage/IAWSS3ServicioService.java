package com.main.skillexchangeapi.domain.abstractions.services.storage;

import com.main.skillexchangeapi.app.constants.ModalidadPagoConstants;
import com.main.skillexchangeapi.app.responses.servicio.MultimediaResourceUploadedResponse;
import com.main.skillexchangeapi.domain.constants.PaymentMethod;
import com.main.skillexchangeapi.domain.exceptions.FileNotFoundException;
import com.main.skillexchangeapi.domain.exceptions.FileNotUploadedException;
import com.main.skillexchangeapi.domain.exceptions.InvalidFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IAWSS3ServicioService {
        List<MultimediaResourceUploadedResponse> uploadMultimediaServiceResources(UUID idServicio,
                        List<MultipartFile> multipartFiles)
                        throws IOException, InvalidFileException, FileNotUploadedException;

        String uploadModalidadPagoResource(UUID idServicio, ModalidadPagoConstants.Tipo paymentMethod,
                        MultipartFile multipartFiles)
                        throws IOException, InvalidFileException, FileNotUploadedException;

        String getFirstImageServicioPresignedUrl(UUID idServicio) throws FileNotFoundException;

        String getImageMetodoPagoPresignedUrl(UUID idServicio,
                        ModalidadPagoConstants.Tipo paymentMethod) throws FileNotFoundException;

        void deleteRecursoMultimediaServicio(UUID idServicio, List<UUID> idsRecurso)
                        throws FileNotFoundException;

        void deleteModalidadPagoYapeResource(UUID idServicio) throws FileNotFoundException;
}
