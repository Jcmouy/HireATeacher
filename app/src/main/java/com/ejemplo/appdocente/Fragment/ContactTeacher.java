package com.ejemplo.appdocente.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ejemplo.appdocente.Adapter.SpinnerAdapterCalendario;
import com.ejemplo.appdocente.Adapter.SpinnerAdapterModalidad;
import com.ejemplo.appdocente.Adapter.SpinnerAdapterNivel;
import com.ejemplo.appdocente.Constants.ConstantNotificacion;
import com.ejemplo.appdocente.DTO.Asignatura;
import com.ejemplo.appdocente.DTO.Calendario;
import com.ejemplo.appdocente.DTO.Docente;
import com.ejemplo.appdocente.DTO.Modalidad;
import com.ejemplo.appdocente.DTO.Nivel;
import com.ejemplo.appdocente.Data.Remote.RemoteUtils;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.R;
import com.ejemplo.appdocente.Remote.DocenteService;
import com.ejemplo.appdocente.Remote.EstudianteService;
import com.ejemplo.appdocente.Util.FormatDate;
import com.google.gson.JsonObject;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;

public class ContactTeacher extends DialogFragment {

    private Spinner spinner_Nivel, spinner_Modalidad, spinner_Horario;
    private Docente docente = null;
    private List<Modalidad> modalidads = new ArrayList<>();
    private List<Nivel> nivels = new ArrayList<>();
    private List<Calendario> calendarioList = new ArrayList<>();
    private LinearLayout lin_money;
    private TextView  text_money;

    private Asignatura asignatura = null;
    private FormatDate formatDate = new FormatDate();
    private Calendario selectedCal;
    private Modalidad selectedModalidad;

    private DocenteService mDocenteService;
    private EstudianteService mEstudianteService;

    private static final int REQUEST_CODE = 1;
    private static final int REQ_CODE_CHECKOUT = 1;
    private static final String REQUESTED_CODE_MESSAGE = "Requested code: ";
    private static final String PAYMENT_WITH_STATUS_MESSAGE = "Payment with status: ";
    private static final String RESULT_CODE_MESSAGE = " Result code: ";

    private View v = null;
    private PrefManager pref;

    public ContactTeacher() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Restore last state for checked position.
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.contact_teacher, null);
        return teacherContact(v);
    }

    private AlertDialog teacherContact(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        spinner_Nivel =  v.findViewById(R.id.spinner_nivel);
        spinner_Modalidad =  v.findViewById(R.id.spinner_modalidad);
        spinner_Horario =  v.findViewById(R.id.spinner_horario);
        lin_money = v.findViewById(R.id.lin_text_money);
        text_money = v.findViewById(R.id.txtMoney);

        Button btnaccept = v.findViewById(R.id.button_contact_accept);
        Button btncancel = v.findViewById(R.id.button_contact_cancel);

        getBundle();
        pref = new PrefManager(getActivity());

        mDocenteService = RemoteUtils.getDocenteService();
        mEstudianteService = RemoteUtils.getEstudianteService();

        SpinnerAdapterNivel adapterNivel = new SpinnerAdapterNivel(getContext(), nivels);
        Nivel fakeNivel = new Nivel(getContext().getResources().getString(R.string.spinner_nivel),null);
        adapterNivel.add(fakeNivel);
        spinner_Nivel.setAdapter(adapterNivel);
        spinner_Nivel.setSelection(adapterNivel.getCount()); //display hint

        SpinnerAdapterModalidad adapterModalidad = new SpinnerAdapterModalidad(getContext(), modalidads);
        Modalidad fakeModalidad = new Modalidad(getContext().getResources().getString(R.string.spinner_modalidad),null);
        adapterModalidad.add(fakeModalidad);
        spinner_Modalidad.setAdapter(adapterModalidad);
        spinner_Modalidad.setSelection(adapterModalidad.getCount()); //display hint

        spinner_Nivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Nivel nivel = (Nivel) adapterView.getItemAtPosition(i);
                if (calendarioList.size() > 0){
                    calendarioList.clear();
                    lin_money.setVisibility(View.GONE);
                }
                getDocenteHorarios(nivel.getNombre());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

        });

        spinner_Modalidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedModalidad = (Modalidad) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_Horario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCal = (Calendario) parent.getItemAtPosition(position);
                if (selectedCal != null){
                    lin_money.setVisibility(View.VISIBLE);
                    text_money.setText(formatDate.doubleMoneyToString(selectedCal.getCostoEstablecido()));
                }
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nivels.remove(fakeNivel);
                modalidads.remove(fakeModalidad);
                dismiss();
            }
        });

        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startCheckout(pref.getEmail(), (int) selectedCal.getCostoEstablecido(), docente.getNombreCompleto());
                //startMercadoPagoCheckout(getString(R.string.mercado_pago_public_key_mine_test), getString(R.string.mercado_pago_checkout_preference_id_mine_test));
            }
        });

        builder.setView(v);

        return builder.create();
    }

    private void startMercadoPagoCheckout(final String checkoutPublicId, final String checkoutPreferenceId) {

        /*
        final Item item = new Item.Builder("Test Title", 3, new BigDecimal(100)).setDescription("description test").build();

        CheckoutPreference.Builder checkoutPreferenceBuilder =  new CheckoutPreference.Builder(Sites.URUGUAY, "a@a.a", Collections.singletonList(item));

         */

        new MercadoPagoCheckout.Builder(checkoutPublicId, checkoutPreferenceId)
                .build()
                .startPayment(requireContext(), REQUEST_CODE);

    }

    private void sendNotification(String idContratacion) {
        setNotificacion(idContratacion, "El usuario xxx lo ha contratado", ConstantNotificacion.CONTRATACION);
        setNotificacionVal(idContratacion, ConstantNotificacion.VALORACION);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                setContratacion(selectedCal.getIdCalendario(), pref.getMobileNumberLogin() ,selectedModalidad.getNombre());
                dismiss();
                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getExtras() != null
                        && data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    final MercadoPagoError mercadoPagoError =
                            (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
                    //Resolve error in checkout
                } else {
                    //Resolve canceled checkout
                }
            }
        }
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null){
            docente = (Docente) bundle.getSerializable("docente");
            asignatura = (Asignatura) bundle.getSerializable("asignatura");
            modalidads = (List<Modalidad>) bundle.getSerializable("modalidads");
            nivels = (List<Nivel>) bundle.getSerializable("nivels");
        }
    }

    private void getDocenteHorarios(String nombreNivel) {
        mDocenteService.teachCalTime(docente.getIdUsuario(), asignatura.getNombre(), nombreNivel).enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                if(response.isSuccessful()) {

                    Log.d("getDocenteHorarios", "Load from API");
                    response.body();

                    JsonObject object = new JsonObject();

                    for (int i = 0; i < response.body().size(); i++){
                        object = response.body().get(i);

                        String textFechaInicio = object.get("FechaInicio").getAsString();
                        Date FechaInicio = formatDate.formatDateFromDB(textFechaInicio);

                        String textFechaFin = object.get("FechaFin").getAsString();
                        Date FechaFin = formatDate.formatDateFromDB(textFechaFin);

                        double costoEstablecido = object.get("CostoEstablecido").getAsDouble();

                        Calendario calendario = new Calendario(object.get("Id").getAsString(), docente, asignatura,
                                FechaInicio, FechaFin, costoEstablecido);

                        calendarioList.add(calendario);
                    }

                    spinnerHorario();

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
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                t.printStackTrace();
                Log.d("getDocenteHorarios", "error loading from API");

            }
        });
    }

    private void startCheckout(String email, int money, String teacher) {
        mEstudianteService.startCallCheckout(email, money, teacher).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    Log.d("getPreferanceID", "Load from API MercadoPago");

                    JsonObject jsonObject = response.body().get("response").getAsJsonObject();
                    String preferanceId =  jsonObject.get("id").getAsString();

                    startMercadoPagoCheckout(getString(R.string.mercado_pago_public_key_mine), preferanceId);

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
                Log.d("getDocenteHorarios", "error loading from API");
            }
        });
    }

    private void setContratacion(String idCalendario, String mobileEstudiante, String nameModalidad) {
        mEstudianteService.setHire(idCalendario, mobileEstudiante, nameModalidad).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    Log.d("setHire", "Insert into Contratacion");

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    String idContratacion =  jsonObject.get("id").getAsString();

                    sendNotification(idContratacion);

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
                Log.d("getDocenteHorarios", "error loading from API");
            }
        });
    }

    private void setNotificacion(String idContratacion, String mensaje, int tipeNotification) {
        mEstudianteService.setHireNotification(idContratacion, mensaje, tipeNotification).enqueue(new Callback<JsonObject>() {
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
                Log.d("getDocenteHorarios", "error loading from API");
            }
        });
    }

    private void setNotificacionVal(String idContratacion, int tipeNotification) {
        mEstudianteService.setNotificationVal(idContratacion, tipeNotification).enqueue(new Callback<JsonObject>() {
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
                Log.d("getDocenteHorarios", "error loading from API");
            }
        });
    }

    public void spinnerHorario(){
        SpinnerAdapterCalendario adapterCalendario = new SpinnerAdapterCalendario(getContext(), calendarioList);
        spinner_Horario.setAdapter(adapterCalendario);
    }
}
