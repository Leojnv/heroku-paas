package edu.pucmm.eict.crud.logic;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class product implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @NotNull
    private String name;
    @NotNull
    private Double price;
    @NotNull
    private Integer amount;

    public product() {

    }

    public product(String name, Double price, Integer amount) {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public product(int id, String name, Double price, Integer amount) {
        this.ID = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void decreaseAmount(int x) {
        amount -= x;
    }

    public void increaseAmount(int x) {
        amount += x;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
