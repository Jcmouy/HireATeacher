package com.ejemplo.appdocente.Database;

import com.ejemplo.appdocente.DTO.NotificationHire;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NotificacionHireDao {

    @Query("SELECT * FROM NOTIFICATIONHIRE")
    boolean checkNotifications();

    @Query("SELECT * FROM NOTIFICATIONHIRE ORDER BY ID")
    List<NotificationHire> loadAllNotificaciones();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotificacion(NotificationHire notificacionHire);

    @Update
    void updateNotificacion(NotificationHire notificacionHire);

    @Delete
    void delete(NotificationHire notificacionHire);

    @Query("DELETE FROM NOTIFICATIONHIRE")
    void deleteAll();

    @Query("SELECT * FROM NOTIFICATIONHIRE WHERE id = :id")
    NotificationHire loadNotificacionById(int id);
}
