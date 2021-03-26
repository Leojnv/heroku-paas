package edu.pucmm.eict.crud.logic;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class comment implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    private user user;
    @ManyToOne(fetch = FetchType.EAGER)
    private product producto;
    private String texto;

    public comment() {

    }

    public comment(user user, product producto, String texto) {
        this.user = user;
        this.producto = producto;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public user getUsuario() {
        return user;
    }

    public void setUsuario(user usuario) {
        this.user = usuario;
    }

    public product getProducto() {
        return producto;
    }

    public void setProducto(product producto) {
        this.producto = producto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
