package edu.pucmm.eict.crud.services;

import edu.pucmm.eict.crud.logic.product;

public class productServices extends DBservices<product> {
    private static productServices instancia;

    private productServices() {
        super(product.class);
    }

    public static productServices getInstancia() {
        if (instancia == null) {
            instancia = new productServices();
        }

        return instancia;
    }
}
