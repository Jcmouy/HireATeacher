package com.ejemplo.appdocente.Controller;

import android.content.Context;

import com.ejemplo.appdocente.DTO.Notificacion;
import com.ejemplo.appdocente.Database.AppDatabase;
import com.ejemplo.appdocente.Database.AppExecutors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NotificationController {
    private AppDatabase mDb;
    private Context context;
    private List<Notificacion> lastNotificaciones;

    public NotificationController(Context context){
        this.context=context;
    }

    public void intiDB() {
        mDb = AppDatabase.getInstance(context);
    }

    public void saveNotificacionDB(Notificacion notificacion) {
        intiDB();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.notificacionDao().insertNotificacion(notificacion);
            }
        });
    }

    public void saveNotificacionesDB(List<Notificacion> notificacions) {
        intiDB();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (Notificacion notif: notificacions){
                  mDb.notificacionDao().insertNotificacion(notif);
                }
            }
        });
    }

    public List<Notificacion>loadAllNotificaciones() {
        intiDB();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<Notificacion>> result = executor.submit(new Callable<List<Notificacion>>() {
            public List<Notificacion> call() throws Exception {
                return mDb.notificacionDao().loadAllNotificaciones();
            }
        });

        try {
            lastNotificaciones = result.get();
        } catch (Exception exception) {
        }
        return lastNotificaciones;
    }

    public void deleteNotificacion(Notificacion deletedItem) {
        intiDB();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.notificacionDao().delete(deletedItem);
            }
        });
    }
}
