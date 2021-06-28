package com.ejemplo.appdocente.DTO;

import java.io.Serializable;
import java.util.Objects;

public class Docente extends Usuario implements Serializable {

    private String idDocente = "";
    private String profesion = "";

    public Docente(String idUsuario, String username, String nombreCompleto, String email, int statis, String mobile, String direccion, int nro, String esquina, String ciudad, String pais, String info, String idDocente, String profesion) {
        super(idUsuario, username, nombreCompleto, email, statis, mobile, direccion, nro, esquina, ciudad, pais, info);
        this.idDocente = idDocente;
        this.profesion = profesion;
    }

    public String getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(String idDocente) {
        this.idDocente = idDocente;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Docente docente = (Docente) o;
        return Objects.equals(idDocente, docente.idDocente) &&
                Objects.equals(profesion, docente.profesion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDocente, profesion);
    }

    @Override
    public String toString() {
        return "Docente{" +
                "idDocente=" + idDocente +
                ", profesion='" + profesion + '\'' +
                '}';
    }


}
