package com.ejemplo.appdocente.DTO;

import java.io.Serializable;
import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notificacion")
public class Notificacion implements Serializable {
    //@PrimaryKey(autoGenerate = true)
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "mensaje")
    private String mensaje = "";
    @ColumnInfo(name = "thumbnail")
    int thumbnail;

    public Notificacion(int id, String mensaje) {
        this.id = id;
        this.mensaje = mensaje;
    }

    @Ignore
    public Notificacion(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notificacion that = (Notificacion) o;
        return id == that.id &&
                Objects.equals(mensaje, that.mensaje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mensaje);
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "id='" + id + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }
}
