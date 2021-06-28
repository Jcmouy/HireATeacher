package com.ejemplo.appdocente.DTO;

import java.util.Date;

public class Calendario {

    private String idCalendario = "";
    private Docente docente;
    private Asignatura asignatura;
    private Date fechaInicio;
    private Date fechaFin;
    private double costoEstablecido;

    public Calendario(String idCalendario, Docente docente, Asignatura asignatura, Date fechaInicio, Date fechaFin, double costoEstablecido) {
        this.idCalendario = idCalendario;
        this.docente = docente;
        this.asignatura = asignatura;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costoEstablecido = costoEstablecido;
    }

    public String getIdCalendario() {
        return idCalendario;
    }

    public void setIdCalendario(String idCalendario) {
        this.idCalendario = idCalendario;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getCostoEstablecido() {
        return costoEstablecido;
    }

    public void setCostoEstablecido(double costoEstablecido) {
        this.costoEstablecido = costoEstablecido;
    }

    @Override
    public String toString() {
        return "Calendario{" +
                "idCalendario='" + idCalendario + '\'' +
                ", docente=" + docente +
                ", asignatura=" + asignatura +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", costoEstablecido=" + costoEstablecido +
                '}';
    }
}
