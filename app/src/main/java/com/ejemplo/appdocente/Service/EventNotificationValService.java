package com.ejemplo.appdocente.Service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.ejemplo.appdocente.Constants.ConstantTipoUsuario;
import com.ejemplo.appdocente.DTO.NotificationRate;
import com.ejemplo.appdocente.Data.Remote.RemoteUtils;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.Remote.EstudianteService;
import com.ejemplo.appdocente.Util.FormatDate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventNotificationValService extends Service {
    NotificationValTask notificacionsValTask;
    private PrefManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        notificacionsValTask = new NotificationValTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificacionsValTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificacionsValTask.cancel(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class NotificationValTask extends AsyncTask<String, String, String> {

        private DateFormat dateFormat;
        private boolean cent;
        private FormatDate formatDate;
        private EstudianteService mEstudianteService;
        private List<NotificationRate> notificacions;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEstudianteService = RemoteUtils.getEstudianteService();
            pref = new PrefManager(getApplicationContext());
            notificacions = new ArrayList<>();
            dateFormat = new SimpleDateFormat("HH:mm:ss");
            formatDate = new FormatDate();
            cent = true;
        }

        @Override
        protected String doInBackground(String... params) {
            //if (!isCancelled()){
            while (cent){

                getNotificationVal(pref.getMobileNumberLogin());

                try {
                    // Stop 10s
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            //}
            }

            return null;
        }

        public void getNotificationVal(String mobileNumberLogin){
            mEstudianteService.getNotificationVal(mobileNumberLogin).enqueue(new Callback<List<JsonObject>>() {
                @Override
                public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                    if(response.isSuccessful()) {

                        JsonObject object = new JsonObject();

                        NotificationRate notification = null;
                        List<NotificationRate> listNotiTeacher = new ArrayList<>();

                        for (int i = 0; i < response.body().size(); i++) {
                            object = response.body().get(i);

                            Set<String> s = object.keySet();

                            for (String v: s){
                                if (v.equals("notifStudent")) {
                                    JsonArray jsonArray = object.get(v).getAsJsonArray();
                                    for (int r = 0; r < jsonArray.size(); r++){
                                        JsonObject jObj = jsonArray.get(r).getAsJsonObject();

                                        String d = jObj.get("FechaFin").getAsString();
                                        Date dateEndClass = formatDate.formatDateFromDB(d);

                                        notification = new NotificationRate(jObj.get("Id").getAsInt(), "",
                                                jObj.get("IdUsuarioValorado").getAsInt(), jObj.get("NombreUsuarioValorado").getAsString(), "", 0, dateEndClass, ConstantTipoUsuario.ESTUDIANTE);
                                    }
                                } else if (v.equals("notifTeacher")) {
                                    NotificationRate notificationTeacher = null;

                                    JsonArray jsonArray = object.get(v).getAsJsonArray();
                                    for (int r = 0; r < jsonArray.size(); r++){
                                        JsonObject jObj = jsonArray.get(r).getAsJsonObject();

                                        String d = jObj.get("FechaFin").getAsString();
                                        Date dateEndClass = formatDate.formatDateFromDB(d);

                                        notificationTeacher = new NotificationRate(jObj.get("Id").getAsInt(), "",
                                                jObj.get("IdUsuarioDisparador").getAsInt(), jObj.get("NombreUsuarioDisparador").getAsString(),
                                                jObj.get("Feedback").getAsString(), jObj.get("FeedbackVal").getAsInt(), dateEndClass, ConstantTipoUsuario.DOCENTE);
                                        listNotiTeacher.add(notificationTeacher);
                                    }
                                }
                            }
                        }

                        Intent intent = new Intent();
                        if (notification != null) {
                            intent.putExtra("NotifVal", (Serializable) notification);
                        }
                        if (listNotiTeacher.size() > 0) {
                            intent.putExtra("NotifValTeacher", (Serializable) listNotiTeacher);
                        }
                        sendBroadcast(intent.setAction("valNewMessage"));

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
