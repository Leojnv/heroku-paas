package edu.pucmm.eict.crud.logic;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class user implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    private String user;
    private String name;

    @NotNull
    private String password;

    public user() {
    }

    public user(String user, String name, String password) {
        this.user = user;
        this.name = name;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
