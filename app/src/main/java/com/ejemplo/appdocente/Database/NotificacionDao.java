package com.ejemplo.appdocente.Database;

import com.ejemplo.appdocente.DTO.Notificacion;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NotificacionDao {

    @Query("SELECT * FROM NOTIFICACION")
    boolean checkNotifications();

    @Query("SELECT * FROM NOTIFICACION ORDER BY ID")
    List<Notificacion> loadAllNotificaciones();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotificacion(Notificacion notificacion);

    @Update
    void updateNotificacion(Notificacion notificacion);

    @Delete
    void delete(Notificacion notificacion);

    @Query("DELETE FROM NOTIFICACION")
    void deleteAll();

    @Query("SELECT * FROM NOTIFICACION WHERE id = :id")
    Notificacion loadNotificacionById(int id);
}
