package com.ejemplo.appdocente.Controller;

import android.content.Context;

import com.ejemplo.appdocente.DTO.NotificationRate;
import com.ejemplo.appdocente.Database.AppDatabase;
import com.ejemplo.appdocente.Database.AppExecutors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NotificationRateController {
    private AppDatabase mDb;
    private Context context;
    private List<NotificationRate> lastNotificaciones;

    public NotificationRateController(Context context){
        this.context=context;
    }

    public void intiDB() {
        mDb = AppDatabase.getInstance(context);
    }

    public void saveNotificacionDB(NotificationRate notificacion) {
        intiDB();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.notificacionRateDao().insertNotificacion(notificacion);
            }
        });
    }

    public void saveNotificacionesDB(List<NotificationRate> notificacions) {
        intiDB();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (NotificationRate notif: notificacions){
                  mDb.notificacionRateDao().insertNotificacion(notif);
                }
            }
        });
    }

    public List<NotificationRate>loadAllNotificaciones() {
        intiDB();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<NotificationRate>> result = executor.submit(new Callable<List<NotificationRate>>() {
            public List<NotificationRate> call() throws Exception {
                return mDb.notificacionRateDao().loadAllNotificaciones();
            }
        });

        try {
            lastNotificaciones = result.get();
        } catch (Exception exception) {
        }
        return lastNotificaciones;
    }

    public List<NotificationRate>loadAllNotificacionesByTipoUsuario(String tipoUsuario) {
        intiDB();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<NotificationRate>> result = executor.submit(new Callable<List<NotificationRate>>() {
            public List<NotificationRate> call() throws Exception {
                return mDb.notificacionRateDao().loadAllNotificacionByTipoUsuario(tipoUsuario);
            }
        });

        try {
            lastNotificaciones = result.get();
        } catch (Exception exception) {
        }
        return lastNotificaciones;
    }

    public void deleteNotificacion(NotificationRate deletedItem) {
        intiDB();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.notificacionRateDao().delete(deletedItem);
            }
        });
    }
}
