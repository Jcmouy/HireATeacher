package com.ejemplo.appdocente.Service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.ejemplo.appdocente.Constants.ConstantTipoUsuario;
import com.ejemplo.appdocente.DTO.Notificacion;
import com.ejemplo.appdocente.DTO.NotificationHire;
import com.ejemplo.appdocente.Data.Remote.RemoteUtils;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.Remote.DocenteService;
import com.ejemplo.appdocente.Remote.EstudianteService;
import com.ejemplo.appdocente.Util.FormatDate;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventNotificationHireService extends Service {
    MyTask myTask;
    private PrefManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        // Toast.makeText(this, "Servicio creado!", Toast.LENGTH_SHORT).show();
        myTask = new MyTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Toast.makeText(this, "Servicio destruído!", Toast.LENGTH_SHORT).show();
        myTask.cancel(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class MyTask extends AsyncTask<String, String, String> {

        private DateFormat dateFormat;
        private String date;
        private boolean cent;
        private EstudianteService mEstudianteService;
        private DocenteService mDocenteService;
        private List<JsonObject> listObject;
        private List<Notificacion> notificacions;
        private String FILTER;
        private FormatDate formatDate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listObject = new ArrayList<>();
            mEstudianteService = RemoteUtils.getEstudianteService();
            mDocenteService = RemoteUtils.getDocenteService();
            pref = new PrefManager(getApplicationContext());
            notificacions = new ArrayList<>();
            dateFormat = new SimpleDateFormat("HH:mm:ss");
            cent = true;
            formatDate = new FormatDate();
        }

        @Override
        protected String doInBackground(String... params) {
            while (cent){
                getNotificationsMsj(pref.getMobileNumberLogin());
                if (pref.getTypeUser().equals(ConstantTipoUsuario.DOCENTE)){
                    getNotifContTeacher(pref.getId());
                }
                try {
                    // Stop 10s
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public void getNotificationsMsj(String mobileNumberLogin){
            mEstudianteService.getNotificationHire(mobileNumberLogin).enqueue(new Callback<List<JsonObject>>() {
                @Override
                public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                    if(response.isSuccessful()) {

                        JsonObject object = new JsonObject();

                        for (int i = 0; i < response.body().size(); i++) {
                            object = response.body().get(i);
                            Notificacion notification = new Notificacion(object.get("Id").getAsInt(), object.get("Mensaje").getAsString());

                            /*
                            if (!notificacions.contains(notification)){
                                notificacions.add(notification);
                            }
                             */

                            if (notificacions.size() > 0){
                                for (Notificacion element : notificacions) {
                                    if (element.getId() != notification.getId()) {
                                        notificacions.add(notification);
                                    }
                                }
                            } else {
                                notificacions.add(notification);
                            }
                        }
                        //listObject = response.body();

                        Intent intent = new Intent(); //FILTER is a string to identify this intent
                        if (notificacions.size() > 0) {
                            intent.putExtra("lista", (Serializable) notificacions);
                        }
                        sendBroadcast(intent.setAction("bcNewMessage"));

                    }else if(response.errorBody() != null){
                        try {
                            String errorBody = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        int statusCode  = response.code();
                    }
                }

                @Override
                public void onFailure(Call<List<JsonObject>> call, Throwable t) {

                }
            });
        }

        public void getNotifContTeacher(String id){
            mDocenteService.teachNotifCont(id).enqueue(new Callback<List<JsonObject>>() {
                @Override
                public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                    if(response.isSuccessful()) {

                        JsonObject object = new JsonObject();

                        List<NotificationHire> listNotiContTeacher = new ArrayList<>();

                        for (int i = 0; i < response.body().size(); i++) {
                            object = response.body().get(i);

                            NotificationHire notificationHire = new NotificationHire(object.get("Id").getAsInt(), "", object.get("NombreEstudiante").getAsString(), object.get("NombreModalidad").getAsString(),
                                    object.get("NombreAsignatura").getAsString(), object.get("HorarioClase").getAsString(), object.get("FechaClase").getAsString(), object.get("CostoEstablecido").getAsInt());

                            listNotiContTeacher.add(notificationHire);
                        }

                        Intent intent = new Intent();
                        if (listNotiContTeacher.size() > 0) {
                            intent.putExtra("NotifContTeacher", (Serializable) listNotiContTeacher);
                        }
                        sendBroadcast(intent.setAction("bcNewMessage"));

                    }else if(response.errorBody() != null){
                        try {
                            String errorBody = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        int statusCode  = response.code();
                    }
                }

                @Override
                public void onFailure(Call<List<JsonObject>> call, Throwable t) {

                }
            });
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getApplicationContext(), "Hora actual: " + values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cent = false;
        }
    }
}
