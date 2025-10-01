package com.main.skillexchangeapi.app.constants;

public abstract class MatchServicioConstants {
    public enum Estado {
        solicitado,
        pendiente_pago,
        rechazado,
        ejecucion,
        finalizado
    }

    public enum Tipo {
        SERVICIO,
        PROYECTO
    }

    public enum Rol {
        CLIENTE,
        PROFESIONAL
    }
}
