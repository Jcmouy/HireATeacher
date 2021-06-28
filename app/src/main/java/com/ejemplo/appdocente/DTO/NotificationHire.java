package com.ejemplo.appdocente.DTO;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "NotificationHire")
public class NotificationHire extends Notificacion implements Serializable {
    @ColumnInfo(name = "nombreEstudiante")
    private String nombreEstudiante;
    @ColumnInfo(name = "nombreModalidad")
    private String nombreModalidad;
    @ColumnInfo(name = "nombreAsignatura")
    private String nombreAsignatura;
    @ColumnInfo(name = "horarioClase")
    private String horarioClase;
    @ColumnInfo(name = "fechaClase")
    private String fechaClase;
    @ColumnInfo(name = "costoEstablecido")
    private int costoEstablecido;

    public NotificationHire(int id, String mensaje, String nombreEstudiante, String nombreModalidad, String nombreAsignatura, String horarioClase, String fechaClase, int costoEstablecido) {
        super(id, mensaje);
        this.nombreEstudiante = nombreEstudiante;
        this.nombreModalidad = nombreModalidad;
        this.nombreAsignatura = nombreAsignatura;
        this.horarioClase = horarioClase;
        this.fechaClase = fechaClase;
        this.costoEstablecido = costoEstablecido;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreModalidad() {
        return nombreModalidad;
    }

    public void setNombreModalidad(String nombreModalidad) {
        this.nombreModalidad = nombreModalidad;
    }

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public String getHorarioClase() {
        return horarioClase;
    }

    public void setHorarioClase(String horarioClase) {
        this.horarioClase = horarioClase;
    }

    public String getFechaClase() {
        return fechaClase;
    }

    public void setFechaClase(String fechaClase) {
        this.fechaClase = fechaClase;
    }

    public int getCostoEstablecido() {
        return costoEstablecido;
    }

    public void setCostoEstablecido(int costoEstablecido) {
        this.costoEstablecido = costoEstablecido;
    }

    @Override
    public String toString() {
        return "NotificationHire{" +
                "nombreEstudiante='" + nombreEstudiante + '\'' +
                ", nombreModalidad='" + nombreModalidad + '\'' +
                ", nombreAsignatura='" + nombreAsignatura + '\'' +
                ", horarioClase='" + horarioClase + '\'' +
                ", fechaClase=" + fechaClase +
                ", costoEstablecido=" + costoEstablecido +
                '}';
    }
}
