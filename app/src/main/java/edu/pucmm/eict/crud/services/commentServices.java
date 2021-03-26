package edu.pucmm.eict.crud.services;

import edu.pucmm.eict.crud.logic.comment;

public class commentServices extends DBservices<comment> {

    private static commentServices instancia;

    private commentServices() {
        super(comment.class);
    }

    public static commentServices getInstancia() {
        if (instancia == null) {
            instancia = new commentServices();
        }

        return instancia;
    }

}
