package edu.pucmm.eict.crud.logic;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class sales implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @NotNull
    private cart cart;
    @NotNull
    private String salesDate;
    @NotNull
    private String user;
    @NotNull
    private Double total;

    public sales() {

    }

    public sales(cart cart, String user, Double total, String date) {
        this.cart = cart;
        this.salesDate = date;// Calendar.getInstance().getTime().toString();
        this.user = user;
        this.total = total;

    }

    public sales(int ID, cart cart, String user, Double total, String date) {
        this.ID = ID;
        this.cart = cart;
        this.salesDate = date;// Calendar.getInstance().getTime().toString();
        this.user = user;
        this.total = total;

    }

    public int getID() {
        return ID;
    }

    public cart getCart() {
        return cart;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public String getUser() {
        return user;
    }

    public Double getTotal() {
        return total;
    }

}
