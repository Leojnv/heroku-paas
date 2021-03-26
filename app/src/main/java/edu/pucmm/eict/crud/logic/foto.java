package edu.pucmm.eict.crud.logic;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class foto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String mimeType;
    @Lob
    private String fotoBase64;

    @ManyToOne
    private product producto;

    public foto() {

    }

    public foto(String nombre, String mimeType, String fotoBase64, product producto) {
        this.nombre = nombre;
        this.mimeType = mimeType;
        this.fotoBase64 = fotoBase64;
        this.producto = producto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
}