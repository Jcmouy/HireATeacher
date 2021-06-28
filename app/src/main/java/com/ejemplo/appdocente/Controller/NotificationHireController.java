package com.ejemplo.appdocente.Controller;

import android.content.Context;

import com.ejemplo.appdocente.DTO.NotificationHire;
import com.ejemplo.appdocente.Database.AppDatabase;
import com.ejemplo.appdocente.Database.AppExecutors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NotificationHireController {
    private AppDatabase mDb;
    private Context context;
    private List<NotificationHire> lastNotificaciones;

    public NotificationHireController(Context context){
        this.context=context;
    }

    public void intiDB() {
        mDb = AppDatabase.getInstance(context);
    }

    public void saveNotificacionDB(NotificationHire notificacion) {
        intiDB();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.notificacionHireDao().insertNotificacion(notificacion);
            }
        });
    }

    public void saveNotificacionesDB(List<NotificationHire> notificacions) {
        intiDB();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (NotificationHire notif: notificacions){
                  mDb.notificacionHireDao().insertNotificacion(notif);
                }
            }
        });
    }

    public List<NotificationHire>loadAllNotificaciones() {
        intiDB();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<NotificationHire>> result = executor.submit(new Callable<List<NotificationHire>>() {
            public List<NotificationHire> call() throws Exception {
                return mDb.notificacionHireDao().loadAllNotificaciones();
            }
        });

        try {
            lastNotificaciones = result.get();
        } catch (Exception exception) {
        }
        return lastNotificaciones;
    }

    public void deleteNotificacion(NotificationHire deletedItem) {
        intiDB();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.notificacionHireDao().delete(deletedItem);
            }
        });
    }
}
