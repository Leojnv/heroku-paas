package edu.pucmm.eict.crud.services;

import edu.pucmm.eict.crud.logic.sales;

public class saleServices extends DBservices<sales> {

    private static saleServices instancia;

    private saleServices() {
        super(sales.class);
    }

    public static saleServices getInstancia() {
        if (instancia == null) {
            instancia = new saleServices();
        }

        return instancia;
    }
}