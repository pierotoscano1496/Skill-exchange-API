package com.main.skillexchangeapi.app.constants;

public abstract class ServicioConstants {
    public enum Modalidad {
        presencial,
        remoto,
        mixto
    }

    public enum TipoPrecio {
        fijo,
        hora,
        rango
    }

    public enum Dia {
        lunes,
        martes,
        miercoles,
        jueves,
        viernes,
        sabado,
        domingo
    }
}
