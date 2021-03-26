package edu.pucmm.eict.crud.logic;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.*;

@Entity
public class cart implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    @OneToMany
    private ArrayList<product> products;

    public cart(ArrayList<product> products) {
        this.products = products;
    }

    public int getID() {
        return ID;
    }

    public ArrayList<product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<product> products) {
        this.products = products;
    }

    public void addToCart(product p) {
        this.products.add(p);
    }

    public void removeFromCart(product p) {
        this.products.remove(p);
    }

    public int searchProduct(int ID) {
        boolean found = false;
        int index = 0;
        while (!found && index < this.products.size()) {
            if (this.products.get(index).getID() == ID) {
                found = true;
            }
            index++;
        }
        return index - 1;

    }

    public double getTotal() {

        double total = 0;
        for (product product : products) {
            total += product.getPrice() * product.getAmount();
        }
        return total;
    };

}
