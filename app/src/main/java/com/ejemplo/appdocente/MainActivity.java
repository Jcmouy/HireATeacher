package com.ejemplo.appdocente;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.ejemplo.appdocente.Constants.ConstantMenu;
import com.ejemplo.appdocente.Constants.ConstantTipoUsuario;
import com.ejemplo.appdocente.Controller.NotificationController;
import com.ejemplo.appdocente.Controller.NotificationHireController;
import com.ejemplo.appdocente.Controller.NotificationRateController;
import com.ejemplo.appdocente.DTO.Notificacion;
import com.ejemplo.appdocente.DTO.NotificationHire;
import com.ejemplo.appdocente.DTO.NotificationRate;
import com.ejemplo.appdocente.Data.Remote.RemoteUtils;
import com.ejemplo.appdocente.Fragment.CommunicationFragment;
import com.ejemplo.appdocente.Fragment.DashboardFragment;
import com.ejemplo.appdocente.Fragment.DashboardTFragment;
import com.ejemplo.appdocente.Fragment.NotificationFragment;
import com.ejemplo.appdocente.Fragment.ProfileFragment;
import com.ejemplo.appdocente.Fragment.RatingsFragment;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.Remote.EstudianteService;
import com.ejemplo.appdocente.Service.EventNotificationHireService;
import com.ejemplo.appdocente.Service.EventNotificationValService;
import com.ejemplo.appdocente.Util.FormatDate;
import com.ejemplo.appdocente.Util.GpsLoc;
import com.google.gson.JsonObject;
import com.hosseiniseyro.apprating.AppRatingDialog;
import com.hosseiniseyro.apprating.listener.RatingDialogListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import it.sephiroth.android.library.bottomnavigation.BadgeProvider;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigation.OnMenuItemSelectionListener,
        RatingDialogListener {

    private String latitude, longitude, msjNotiVal;
    private String lat,lon;
    private List<Notificacion> notificacions, last_notificacions;
    private NotificationRate notifRate;
    private List<NotificationRate> listNotiTeacher = new ArrayList<>();
    private List<NotificationHire> listNotiContTeacher = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;
    private BadgeProvider provider;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private EstudianteService mEstudianteService;
    private int intervalDays;
    private FormatDate formatDate = new FormatDate();
    private int intervalDaysRate;
    private PrefManager pref;
    private NotificationRateController notiRateController;
    private NotificationHireController notiHireController;
    private NotificationController notiController;
    private List<String> listFragments  = new ArrayList<>();

    private String MAIN_FRAG_NOTIF = "mainFragNotification";
    private String MAIN_FRAG_RATIG = "mainFragRating";
    private String MAIN_FRAG_DASH = "mainFragDashboard";
    private String MAIN_FRAG_DASHT = "mainFragDashboardT";
    private String MAIN_FRAG_COMM = "mainFragComm";
    private String MAIN_FRAG_PROF = "mainFragProfile";
    private String currentFragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        GpsLoc gpsLoc = new GpsLoc();
        gpsLoc.getDeviceLocation(MainActivity.this);
        latitude = gpsLoc.getLatitude();
        longitude = gpsLoc.getLongitude();
        pref = new PrefManager(this);
        notiRateController = new NotificationRateController(getApplicationContext());
        notiHireController = new NotificationHireController(getApplicationContext());
        notiController = new NotificationController(getApplicationContext());
        AndroidThreeTen.init(this);

        CoordinatorLayout coordinatorLayout = findViewById(R.id.main_coordinator_container);
        FrameLayout frameLayout = findViewById(R.id.main_frame_container);

        BottomNavigation bottomNavigation = findViewById(R.id.main_BottomNavigation);

        if (pref.getTypeUser().equals(ConstantTipoUsuario.DOCENTE)){
            bottomNavigation.inflateMenu(R.menu.bottombar_menu_5item_teacher);
        }

        fragmentManager = this.getSupportFragmentManager();
        loadFragments();

        provider = bottomNavigation.getBadgeProvider();
        mEstudianteService = RemoteUtils.getEstudianteService();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
        bottomNavigation.setDefaultTypeface(typeface);

        bottomNavigation.setMenuItemSelectionListener(this);

        bottomNavigation.setMenuChangedListener(new BottomNavigation.OnMenuChangedListener() {
            @Override
            public void onMenuChanged(@NotNull BottomNavigation bottomNavigation) {
                menuSelected(bottomNavigation.getSelectedIndex());
            }
        });

        //Seteo por defecto el mapa
        bottomNavigation.setDefaultSelectedIndex(2);

        this.registerReceiver(this.broadCastNewMessage, new IntentFilter("bcNewMessage"));
        this.registerReceiver(this.broadCastvalNewMessage, new IntentFilter("valNewMessage"));
        notificacions = new ArrayList<>();
        last_notificacions = new ArrayList<>();

        startService(new Intent(MainActivity.this, EventNotificationValService.class));
    }

    @Override
    public void onMenuItemReselect(int i, int indexSelected, boolean b) {
        menuSelected(indexSelected);
    }

    @Override
    public void onMenuItemSelect(int i, int indexSelected, boolean b) {
        menuSelected(indexSelected);
    }

    public void menuSelected(int selectedIndex){
        switch (selectedIndex){
            case ConstantMenu.NOTIFICACION:
                fragment = new NotificationFragment();
                if (provider.hasBadge(R.id.bbn_item1)){
                    for (Notificacion notif: notificacions){
                        changeStateNotif(notif.getId());
                    }
                    provider.remove(R.id.bbn_item1);
                    /*
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notificacions", (Serializable) notificacions);
                    fragment.setArguments(bundle);

                     */
                }
                setFragment(fragment, MAIN_FRAG_NOTIF);
                break;
            case ConstantMenu.VALORACION:
                fragment = new RatingsFragment();
                if (provider.hasBadge(R.id.bbn_item2)){
                    provider.remove(R.id.bbn_item2);
                }
                setFragment(fragment, MAIN_FRAG_RATIG);
                break;
            case ConstantMenu.DASHBOARD:
                if (pref.getTypeUser().equals(ConstantTipoUsuario.ESTUDIANTE)){
                    Bundle bundle = new Bundle();
                    bundle.putString("latitude",latitude);
                    bundle.putString("longitude",longitude);

                    fragment = new DashboardFragment();
                    fragment.setArguments(bundle);
                    setFragment(fragment, MAIN_FRAG_DASH);
                    break;
                } else if (pref.getTypeUser().equals(ConstantTipoUsuario.DOCENTE)){
                    fragment = new DashboardTFragment();
                    setFragment(fragment, MAIN_FRAG_DASHT);
                    break;
                }
            case ConstantMenu.MENSAJE:
                fragment = new CommunicationFragment();
                setFragment(fragment, MAIN_FRAG_COMM);
                break;
            case ConstantMenu.PERFIL:
                fragment = new ProfileFragment();
                setFragment(fragment, MAIN_FRAG_PROF);
                break;
        }
    }

    private void loadFragments() {
        listFragments.add(MAIN_FRAG_COMM);
        listFragments.add(MAIN_FRAG_PROF);
        listFragments.add(MAIN_FRAG_DASHT);
        listFragments.add(MAIN_FRAG_DASH);
        listFragments.add(MAIN_FRAG_RATIG);
        listFragments.add(MAIN_FRAG_NOTIF);
    }

    private void setFragment(Fragment fragment, String nameFrag) {
        currentFragment = nameFrag;
        if (fragmentManager.findFragmentByTag(nameFrag) != null) {
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(nameFrag)).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.main_frame_container, fragment, nameFrag).commit();
        }
        for (String frag: listFragments) {
            if (!frag.equals(nameFrag)){
                if (fragmentManager.findFragmentByTag(frag) != null) {
                    fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(frag)).commit();
                }
            }
        }
    }



    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private BroadcastReceiver broadCastNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Set<String> keySet = extras.keySet();
                for (String key: keySet){
                    if (key.equals("lista")) {
                        notificacions = (List<Notificacion>) extras.getSerializable("lista");
                        saveListNotiAfterHireTeacherDB();
                        /*
                        if (notificacions.size() > 0 && last_notificacions.size() == 0){
                            last_notificacions = notificacions;
                            provider.show(R.id.bbn_item1);
                        } else if (!compare_notifications()){
                            provider.show(R.id.bbn_item1);
                            last_notificacions = notificacions;
                        }
                         */
                    } else if (key.equals("NotifContTeacher")){
                        listNotiContTeacher = (List<NotificationHire>) extras.getSerializable("NotifContTeacher");
                        saveListNotiContTeacherDB();
                    }
                }
            }
        }
    };

    public boolean compare_notifications(){
        for (int i=0; i < notificacions.size(); i++){
            for (int z=0; z < last_notificacions.size(); z++){
                if (!notificacions.get(i).equals(last_notificacions.get(z))){
                    return false;
                }
            }
        }
        return true;
    }

    private void changeStateNotif(int id) {
        mEstudianteService.changeNotifStateEstudiante(id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    Log.d("changeStateNotif", "Notifaction State changed");

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
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Log.d("changeStateNotif", "error loading from API");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        startService(new Intent(MainActivity.this, EventNotificationHireService.class));
    }

    @Override
    public void onPause() {
        super.onPause();
        stopService(new Intent(MainActivity.this, EventNotificationHireService.class));
    }

    @Override
    public void onStop() {
        super.onStop();
        stopService(new Intent(MainActivity.this, EventNotificationHireService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(broadCastNewMessage);
        this.unregisterReceiver(broadCastvalNewMessage);
    }

    private BroadcastReceiver broadCastvalNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean initDialogRating = false;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Set<String> keySet = extras.keySet();
                for (String key: keySet){
                    if (key.equals("NotifVal")) {
                        notifRate = (NotificationRate) extras.getSerializable("NotifVal");
                        initDialogRating = true;
                    } else if (key.equals("NotifValTeacher")) {
                        listNotiTeacher = (List<NotificationRate>) extras.getSerializable("NotifValTeacher");
                        saveListNotiTeacherDB();
                    }
                }
            }


            if (notifRate != null) {
                LocalDate start = LocalDate.now();
                LocalDate end = formatDate.convertToLocalDateViaMilisecond(notifRate.getFechaNotificacion());
                intervalDaysRate = (int) ChronoUnit.DAYS.between(start, end);
                // Se le agrega un dia para pedir que califique al docente
                intervalDaysRate += 1;
            }

            // intervalDaysRate = 0;

            if (intervalDaysRate == 0 && initDialogRating) {
                showDialogRating();
            }

            /*
            else {
                stopRateTeacher();
            }
             */
        }
    };

    private void showDialogRating() {
        if (pref.getTypeUser().equals(ConstantTipoUsuario.ESTUDIANTE)){
            msjNotiVal = getResources().getString(R.string.btt_teacher).toLowerCase();
        } else if (pref.getTypeUser().equals(ConstantTipoUsuario.DOCENTE)) {
            msjNotiVal = getResources().getString(R.string.btt_student).toLowerCase();
        }
        buildRatingDialog().showRateDialogIfMeetsConditions();
        // stopRateTeacher();
    }

    private AppRatingDialog buildRatingDialog() {
        return new AppRatingDialog.Builder()
                .setPositiveButtonText(getResources().getString(R.string.dialog_rate_button_submit))
                .setNeutralButtonText(getResources().getString(R.string.dialog_rate_button_neutral))
                .setNoteDescriptions(Arrays.asList(getResources().getString(R.string.dialog_rate_description_very_bad),
                        getResources().getString(R.string.dialog_rate_description_bad), getResources().getString(R.string.dialog_rate_description_quite_good),
                        getResources().getString(R.string.dialog_rate_description_very_good), getResources().getString(R.string.dialog_rate_description_excelent)))
                .setDefaultRating(2)
                .setThreshold(4)
                .setAfterInstallDay(intervalDaysRate)
                .setRemindInterval(1)
                .setTitle(getResources().getString(R.string.dialog_rate_title) +" "+ msjNotiVal
                        +":"+" "+notifRate.getNombreUsuarioValorado())
                .setDescription(getResources().getString(R.string.dialog_rate_description))
                .setStarColor(R.color.starColor2)
                .setNoteDescriptionTextColor(R.color.noteDescriptionTextColor2)
                .setTitleTextColor(R.color.titleTextColor2)
                .setDescriptionTextColor(R.color.descriptionTextColor2)
                .setCommentTextColor(R.color.commentTextColor2)
                .setCommentBackgroundColor(R.color.commentBackgroundColor2)
                .setDialogBackgroundColor(R.color.rateAppDialogBackgroundColor)
                .setWindowAnimation(R.style.MyDialogSlideHorizontalAnimation)
                .setHint(getResources().getString(R.string.dialog_rate_hint))
                .setHintTextColor(R.color.hintTextColor2)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(this);
    }

    private void stopRateTeacher() {
        this.unregisterReceiver(this.broadCastvalNewMessage);
        stopService(new Intent(MainActivity.this, EventNotificationValService.class));
    }

    private void saveListNotiAfterHireTeacherDB() {
        List<Notificacion> dbListNotiAfterHire = new ArrayList<>();
        dbListNotiAfterHire = notiController.loadAllNotificaciones();
        if (dbListNotiAfterHire.size() > 0){
            for (Notificacion not : notificacions) {
                if (!dbListNotiAfterHire.contains(not)){
                    notiController.saveNotificacionDB(not);
                    provider.show(R.id.bbn_item1);
                }
            }
        } else {
            notiController.saveNotificacionesDB(notificacions);
            provider.show(R.id.bbn_item1);
        }
    }

    private void saveListNotiTeacherDB() {
        List<NotificationRate> dbListNoti = new ArrayList<>();
        dbListNoti = notiRateController.loadAllNotificaciones();
        if (dbListNoti.size() > 0){
            for (NotificationRate notiRateQ : listNotiTeacher) {
                if (!dbListNoti.contains(notiRateQ)){
                    notiRateController.saveNotificacionDB(notiRateQ);
                    provider.show(R.id.bbn_item2);
                }
            }
        } else {
            notiRateController.saveNotificacionesDB(listNotiTeacher);
            provider.show(R.id.bbn_item2);
        }
    }

    private void saveListNotiContTeacherDB() {
        List<NotificationHire> dbListNotiCont = new ArrayList<>();
        dbListNotiCont = notiHireController.loadAllNotificaciones();
        if (dbListNotiCont.size() > 0){
            for (NotificationHire notiRateQ : listNotiContTeacher) {
                if (!dbListNotiCont.contains(notiRateQ)){
                    notiHireController.saveNotificacionDB(notiRateQ);
                }
            }
        } else {
            notiHireController.saveNotificacionesDB(listNotiContTeacher);
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClickedWithComment(int rate, @NotNull String comment) {
        notifRate.setFeedbackVal(rate);
        notifRate.setFeedback(comment);
        notiRateController.saveNotificacionDB(notifRate);
        sendFeedback(rate,comment);
    }

    @Override
    public void onPositiveButtonClickedWithoutComment(int rate) {
        notifRate.setFeedbackVal(rate);
        notiRateController.saveNotificacionDB(notifRate);
        sendFeedback(rate,null);
    }

    private void sendFeedback(int rate, String comment) {
        mEstudianteService.setFeedbackTeacher(notifRate.getId(),comment,rate).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    Log.d("setHire", "Insert into Contratacion");

                }else if(response.errorBody() != null){
                    try {
                        String errorBody = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                Log.d("setFeedbackTeacher", "error sending feedback");
            }
        });
    }
}
