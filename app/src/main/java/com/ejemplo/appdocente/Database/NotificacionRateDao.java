package com.ejemplo.appdocente.Database;

import com.ejemplo.appdocente.DTO.NotificationRate;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NotificacionRateDao {

    @Query("SELECT * FROM NOTIFICATIONRATE")
    boolean checkNotifications();

    @Query("SELECT * FROM NOTIFICATIONRATE ORDER BY ID")
    List<NotificationRate> loadAllNotificaciones();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotificacion(NotificationRate notificacionRate);

    @Update
    void updateNotificacion(NotificationRate notificacionRate);

    @Delete
    void delete(NotificationRate notificacionRate);

    @Query("DELETE FROM NOTIFICATIONRATE")
    void deleteAll();

    @Query("SELECT * FROM NOTIFICATIONRATE WHERE id = :id")
    NotificationRate loadNotificacionById(int id);

    @Query("SELECT * FROM NOTIFICATIONRATE WHERE tipoUsuario = :tipoUsuario")
    List<NotificationRate> loadAllNotificacionByTipoUsuario(String tipoUsuario);
}
