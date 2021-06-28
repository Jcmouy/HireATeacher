package com.ejemplo.appdocente.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Asignatura implements Serializable {

    private String idAsignatura = "";
    private String nombre = "";
    private Docente docente;
    private List<Nivel> listNivel = new ArrayList<>();
    private List<Modalidad> listModalidad = new ArrayList<>();

    public Asignatura(String idAsignatura, String nombre, Docente docente, List<Nivel> listNivel, List<Modalidad> listModalidad) {
        this.idAsignatura = idAsignatura;
        this.nombre = nombre;
        this.docente = docente;
        this.listNivel = listNivel;
        this.listModalidad = listModalidad;
    }

    public String getIdAsignatura() {
        return idAsignatura;
    }

    public void setIdAsignatura(String idAsignatura) {
        this.idAsignatura = idAsignatura;
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

    public List<Nivel> getListNivel() {
        return listNivel;
    }

    public void setListNivel(List<Nivel> listNivel) {
        this.listNivel = listNivel;
    }

    public List<Modalidad> getListModalidad() {
        return listModalidad;
    }

    public void setListModalidad(List<Modalidad> listModalidad) {
        this.listModalidad = listModalidad;
    }

    @Override
    public String toString() {
        return "Asignatura{" +
                "nombre='" + nombre + '\'' +
                ", docente=" + docente +
                ", listNivel=" + listNivel +
                ", listModalidad=" + listModalidad +
                '}';
    }
}
