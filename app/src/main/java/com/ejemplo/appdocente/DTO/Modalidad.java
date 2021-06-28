package com.ejemplo.appdocente.DTO;

import java.io.Serializable;

public class Modalidad implements Serializable {

    private String nombre = "";
    private Docente docente;

    public Modalidad(String nombre, Docente docente) {
        this.nombre = nombre;
        this.docente = docente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    @Override
    public String toString() {
        return "Modalidad{" +
                "nombre='" + nombre + '\'' +
                ", docente=" + docente +
                '}';
    }
}
