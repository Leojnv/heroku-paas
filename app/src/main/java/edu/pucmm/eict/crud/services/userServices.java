package edu.pucmm.eict.crud.services;

import edu.pucmm.eict.crud.logic.user;

public class userServices extends DBservices<user> {

    private static userServices instancia;

    private userServices() {
        super(user.class);
    }

    public static userServices getInstancia() {
        if (instancia == null) {
            instancia = new userServices();
        }

        return instancia;
    }

    public user verifyUser(String password, String user) {

        user aux_user = find(user);
        if (aux_user != null && aux_user.getPassword().equals(password)) {
            return aux_user;
        }
        return null;

    }
}