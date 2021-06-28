package com.ejemplo.appdocente.DTO;

import com.ejemplo.appdocente.Util.FormatDate;

import java.io.Serializable;
import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

@Entity(tableName = "NotificationRate")
public class NotificationRate extends Notificacion implements Serializable {
    @ColumnInfo(name = "idUsuario")
    private int idUsuario;
    @ColumnInfo(name = "nombreUsuarioValorado")
    private String nombreUsuarioValorado;
    @ColumnInfo(name = "feedback")
    private String feedback;
    @ColumnInfo(name = "feedbackVal")
    private int feedbackVal;
    @ColumnInfo(name = "fechaNotificacion")
    @TypeConverters({FormatDate.class})
    private Date fechaNotificacion;
    @ColumnInfo(name = "tipoUsuario")
    private String tipoUsuario;

    public NotificationRate(int id, String mensaje, int idUsuario, String nombreUsuarioValorado, String feedback, int feedbackVal, Date fechaNotificacion, String tipoUsuario) {
        super(id, mensaje);
        this.idUsuario = idUsuario;
        this.nombreUsuarioValorado = nombreUsuarioValorado;
        this.feedback = feedback;
        this.feedbackVal = feedbackVal;
        this.fechaNotificacion = fechaNotificacion;
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuarioValorado() {
        return nombreUsuarioValorado;
    }

    public void setNombreUsuarioValorado(String nombreUsuarioValorado) {
        this.nombreUsuarioValorado = nombreUsuarioValorado;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getFeedbackVal() {
        return feedbackVal;
    }

    public void setFeedbackVal(int feedbackVal) {
        this.feedbackVal = feedbackVal;
    }

    public Date getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(Date fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Override
    public String toString() {
        return "NotificationRate{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuarioValorado='" + nombreUsuarioValorado + '\'' +
                ", feedback='" + feedback + '\'' +
                ", feedbackVal=" + feedbackVal +
                ", fechaNotificacion=" + fechaNotificacion +
                ", tipoUsuario=" + tipoUsuario +
                '}';
    }
}
