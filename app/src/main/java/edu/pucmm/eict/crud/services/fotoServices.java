package edu.pucmm.eict.crud.services;

import edu.pucmm.eict.crud.logic.foto;

public class fotoServices extends DBservices<foto> {

    private static fotoServices instancia;

    private fotoServices() {
        super(foto.class);
    }

    public static fotoServices getInstancia() {
        if (instancia == null) {
            instancia = new fotoServices();
        }

        return instancia;
    }

}
