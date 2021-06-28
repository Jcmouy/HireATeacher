package com.ejemplo.appdocente.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ejemplo.appdocente.Constants.ConstantFiltro;
import com.ejemplo.appdocente.DTO.Asignatura;
import com.ejemplo.appdocente.DTO.Docente;
import com.ejemplo.appdocente.DTO.LocalizacionUsuario;
import com.ejemplo.appdocente.DTO.Modalidad;
import com.ejemplo.appdocente.DTO.Nivel;
import com.ejemplo.appdocente.DTO.Usuario;
import com.ejemplo.appdocente.Data.Remote.RemoteUtils;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.R;
import com.ejemplo.appdocente.Remote.EstudianteService;
import com.ejemplo.appdocente.Util.FormatDate;
import com.ejemplo.appdocente.Util.FormatText;
import com.ejemplo.appdocente.Util.GpsLoc;
import com.ejemplo.appdocente.Util.TimePicker;
import com.ejemplo.appdocente.Util.Tooltip;
import com.ejemplo.appdocente.Util.UBubbleShowCase;
import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.JsonObject;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

import androidx.annotation.AttrRes;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements OnMapReadyCallback,
                        FilerLevMod.FilterLevModInterface, FilerCosto.FilterCostoInterface, TimePicker.TimePickerInterface{

    private GoogleMap mMap;
    private Double latitude, longitude;
    private String direccion, latStandar, lonStandar;
    private TextView txtDireccion;
    private LinearLayout lin, lin3,lin4,layout_filtro_estudiante, search_box;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton floatingActionCosto, floatingActionNivMod,
            floatingActionFecha, floatingActionHorario, FloatingActionUbicacion;
    private Typeface typeface;

    private CardView cardInitalAddress;

    private CameraPosition mCameraPosition;
    private LatLng location;
    private LatLng locationFilter;
    private static final int DEFAULT_ZOOM = 15;
    private static final String TAG = "MapsActivity";
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private GpsLoc gpsLoc;

    private EstudianteService mEstudianteService;
    private PrefManager pref;

    private Button btnSeleccionarDireccion, btnGuardarDireccion, btnVerificarDireccion;
    private InputMethodManager imm = null;

    private Marker asigMarker;
    private ArrayList<Marker> list_markerAsig = new ArrayList<>();

    private List<Usuario> usuarioList = new ArrayList<>();
    private List<LocalizacionUsuario> localizacionList = new ArrayList<>();
    private List<LocalizacionUsuario> localizacionListFiltered = new ArrayList<>();
    private List<String> constListSetFiltered = new ArrayList<>();
    private List<Nivel> nivelList = new ArrayList<>();
    private List<Modalidad> modalidadList = new ArrayList<>();
    private Asignatura asignatura = null;

    private boolean NivModFilter, CostoFilter, HorarioFilter,
            FechaFilter, UbiacionFilter, InitFilter = false;
    private int costoIni, costoFin;
    private List<String> nivSelec, modSelec;
    private Date selectedDateStart, selectedDateEnd ;
    private Date dateStartTime, dateEndTime;
    private FormatDate formatDate = new FormatDate();

    public DashboardFragment() {
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(requireContext());
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        search_box = (LinearLayout) view.findViewById(R.id.lin_search_box);
        cardInitalAddress = (CardView) view.findViewById(R.id.card_initial_address);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapNav);
        mapFragment.getMapAsync(this);

        pref = new PrefManager(getActivity());
        imm = (InputMethodManager) getActivity().getSystemService(requireContext().INPUT_METHOD_SERVICE);

        SearchView searchAsig = view.findViewById(R.id.search_mat);
        txtDireccion = view.findViewById(R.id.txtDireccion);
        btnVerificarDireccion = view.findViewById(R.id.btnVerificarDireccion);

        getBundle();

        btnGuardarDireccion = view.findViewById(R.id.btnSaveAddress);
        btnSeleccionarDireccion = view.findViewById(R.id.btnFindaddress);
        lin = view.findViewById(R.id.layout_Aquiresides);
        lin3 = view.findViewById(R.id.layout_lin3);
        lin4 = view.findViewById(R.id.layout_lin4);
        layout_filtro_estudiante = view.findViewById(R.id.layout_filtro_estudiante);
        floatingActionMenu = view.findViewById(R.id.fab);
        floatingActionCosto = view.findViewById(R.id.fab_item1);
        floatingActionNivMod = view.findViewById(R.id.fab_item2);
        floatingActionFecha = view.findViewById(R.id.fab_item3);
        floatingActionHorario = view.findViewById(R.id.fab_item4);
        FloatingActionUbicacion = view.findViewById(R.id.fab_item5);

        int dialogTheme = resolveOrThrow(getContext(), R.attr.materialCalendarTheme);
        // int fullscreenTheme = resolveOrThrow(getContext(), R.attr.materialCalendarFullscreenTheme);

        if (pref.isFirstTimeLaunch()) {
            UBubbleShowCase uBubbleShowCase =  new UBubbleShowCase(requireContext(), new WeakReference<FragmentActivity>(requireActivity()), view);
            uBubbleShowCase.startShowCase();
            pref.setFirstTimeLaunch(false);
        }

        mEstudianteService = RemoteUtils.getEstudianteService();

        btnGuardarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerLatLon(latitude,longitude);
            }
        });

        btnVerificarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ingresarDir();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSeleccionarDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pantallaBuscarDireccion(true);
            }
        });

        searchAsig.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                searchAsig.clearFocus();
                if (list_markerAsig.size() > 0){
                    clearData();
                }
                getLocationAsig(FormatText.unAccent(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int i = newText.trim().length();
                if (i == 0){
                    clearData();
                }
                return false;
            }
        });

        searchAsig.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast t = Toast.makeText(requireContext(), "close", Toast.LENGTH_SHORT);
                t.show();

                return false;
            }
        });

        floatingActionCosto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CostoFilter) {
                    FilerCosto dialogFilerCosto = new FilerCosto();
                    dialogFilerCosto.show(getChildFragmentManager(), "FilerCosto");
                } else {
                    clearFilter(ConstantFiltro.COSTO);
                }
            }
        });

        floatingActionNivMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NivModFilter){
                    FilerLevMod dialogFilerLevMod = new FilerLevMod();
                    dialogFilerLevMod.show(getChildFragmentManager(), "FilerLevMod");
                } else {
                    clearFilter(ConstantFiltro.NIVMOD);
                }
            }
        });

        floatingActionFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FechaFilter){
                    MaterialDatePicker.Builder<Pair<Long, Long>> builder =
                            MaterialDatePicker.Builder.dateRangePicker();

                    builder.setTheme(dialogTheme);
                    builder.setTitleText(R.string.filtro_fecha);

                    CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Montevideo"));

                    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

                    constraintsBuilder.setStart(calendar.getTimeInMillis());
                    calendar.roll(Calendar.YEAR, 1);
                    constraintsBuilder.setEnd(calendar.getTimeInMillis());
                    builder.setCalendarConstraints(constraintsBuilder.build());



                    MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
                    picker.show(getChildFragmentManager(), picker.toString());

                    picker.addOnPositiveButtonClickListener(
                            selection -> {
                                OnSetDatePicker(selection);
                            });
                    picker.addOnNegativeButtonClickListener(
                            dialog -> {
                            });
                    picker.addOnCancelListener(
                            dialog -> {
                            });
                } else {
                    clearFilter(ConstantFiltro.FECHA);
                }
            }
        });

        floatingActionHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HorarioFilter){
                    TimePicker timePicker = new TimePicker(requireContext(), new WeakReference<FragmentActivity>(requireActivity()), DashboardFragment.this);
                    timePicker.getTimePicker();
                } else {
                    clearFilter(ConstantFiltro.HORARIO);
                }
            }
        });

        FloatingActionUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UbiacionFilter){
                    setUbicacionFilter();
                } else {
                    clearFilter(ConstantFiltro.UBICACION);
                }
            }
        });

        // Habilitar para utilizar smartphone
        /////////////////////////////////////////////////////////
        /*

        if (pref.getLatUser() != null && pref.getLngUser() != null){
            initalLoginScreen();
        }else{
            Toast.makeText(getContext(),"Ingresé su dirección por favor",Toast.LENGTH_LONG).show();
        }

         */
        /////////////////////////////////////////////////////////

        // Habilitar para Emular Andorid
        /////////////////////////////////////////////////////////

        // pref.createLogin("78","Alumno Prueba","AlumPru","jccc@gmail.com","+59899471402", ConstantTipoUsuario.ESTUDIANTE);
        initalLoginScreen();

        /////////////////////////////////////////////////////////

        return view;
    }

    private static int resolveOrThrow(Context context, @AttrRes int attributeResId) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attributeResId, typedValue, true)) {
            return typedValue.data;
        }
        throw new IllegalArgumentException(context.getResources().getResourceName(attributeResId));
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null){
            latitude = Double.valueOf(Objects.requireNonNull(bundle.getString("latitude")));
            longitude = Double.valueOf(Objects.requireNonNull(bundle.getString("longitude")));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.nav_work_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Actual position in the world map
        LatLng actual = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(actual).title("Tu ubicación"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(actual));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(actual.latitude,actual.longitude),DEFAULT_ZOOM));

        /*
        mMap.addMarker(new MarkerOptions().position(p1).title(direccion));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p1));
         */

        //Click on marker
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Marker asigMarkerShow = null;
                int distOld = 0;
                gpsLoc = new GpsLoc();
                for (int i =0; i < list_markerAsig.size(); i++){
                    asigMarker = list_markerAsig.get(i);

                    if (asigMarkerShow == null) {
                        asigMarkerShow = asigMarker;
                        distOld = gpsLoc.calculateDistanceInMeter(latLng.latitude, latLng.longitude, asigMarkerShow.getPosition().latitude, asigMarkerShow.getPosition().longitude);
                    } else {
                        int dist = gpsLoc.calculateDistanceInMeter(latLng.latitude, latLng.longitude, asigMarker.getPosition().latitude, asigMarker.getPosition().longitude);
                        if (distOld > dist) {
                            asigMarkerShow = asigMarker;
                        }
                    }
                }
                onMarkerLongClick(asigMarkerShow);
            }
        });
    }

    public void pantallaBuscarDireccion(boolean buscar){
        if (buscar){
            lin3.setVisibility(View.GONE);
            lin4.setVisibility(View.VISIBLE);
        }else{
            lin3.setVisibility(View.VISIBLE);
            lin4.setVisibility(View.GONE);
        }
    }

    public void updateLocation(boolean filter){
        LatLng loc;
        if (!filter){
            if (location == null) {
                gpsLoc = new GpsLoc();
                gpsLoc.getDeviceLocation(requireContext());
                Double lat = Double.valueOf(gpsLoc.getLatitude());
                Double lon = Double.valueOf(gpsLoc.getLongitude());
                location = new LatLng(lat, lon);
            }
            loc = location;
        }else {
            loc = locationFilter;
        }
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (loc != null) {
            mMap.addMarker(new MarkerOptions().position(loc).title("Ubicación Actual"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.latitude,loc.longitude),DEFAULT_ZOOM));

        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        }
    }

    public void ingresarDir() throws IOException {

        direccion = txtDireccion.getText().toString();

        Geocoder coder = new Geocoder(getActivity());
        LatLng p1 = null;

        gpsLoc = new GpsLoc();
        gpsLoc.getDeviceLocation(getActivity());
        latStandar = gpsLoc.getLatitude();
        lonStandar = gpsLoc.getLongitude();

        if (!direccion.equals("")){
            List<Address> address = coder.getFromLocationName(direccion, 1);
            if (address == null) {
                LatLng standar = new LatLng(Double.valueOf(latStandar), Double.valueOf(lonStandar));
            } else {
                Address locationA = address.get(0);
                location = new LatLng(locationA.getLatitude(), locationA.getLongitude());
                updateLocation(false);
                pantallaBuscarDireccion(false);
            }
        }

    }

    public void obtenerLatLon(double latitude, double longitude){

        final double lat = latitude;
        final double lng = longitude;
        String mobile = pref.getMobileNumber();

        mEstudianteService.setLocationEstudiante(mobile,lat,lng).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {

                    pref.setLatLngUser(String.valueOf(lat),String.valueOf(lng));
                    initalLoginScreen();
                    Toast.makeText(getActivity(), getResources().getString(R.string.msg_dire_ing), Toast.LENGTH_SHORT).show();

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

            }
        });
    }

    public void getLocationAsig(final String query){
        mEstudianteService.locateAsig(query).enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                if(response.isSuccessful()) {

                    JsonObject object = new JsonObject();

                    for (int i = 0; i < response.body().size(); i++){

                        object = response.body().get(i);

                        Docente docente = new Docente(
                                object.get("Id").getAsString(), object.get("Username").getAsString(), object.get("NombreCompleto").getAsString(), object.get("Email").getAsString(),
                                object.get("Status").getAsInt(),
                                !object.get("Mobile").isJsonNull() ? object.get("Mobile").getAsString() : "",
                                !object.get("Direccion").isJsonNull() ? object.get("Direccion").getAsString() : "",
                                !object.get("Nro").isJsonNull() ? object.get("Nro").getAsInt() : 0,
                                !object.get("Esquina").isJsonNull() ? object.get("Esquina").getAsString() : "",
                                !object.get("Ciudad").isJsonNull() ? object.get("Ciudad").getAsString() : "",
                                !object.get("Pais").isJsonNull() ? object.get("Pais").getAsString() : "",
                                !object.get("Info").isJsonNull() ? object.get("Info").getAsString() : "",
                                object.get("DocenteId").getAsString(),
                                !object.get("Profesion").isJsonNull() ? object.get("Profesion").getAsString() : "");

                        usuarioList.add(docente);

                        Set<String> keys = object.keySet();
                        Pattern p = Pattern.compile("\\b(\\w*Nivel\\w*)\\b");
                        for (String w : keys) {
                            if (p.matcher(w).find()){
                                Nivel nivel = new Nivel( object.get(w).getAsString(), docente);
                                nivelList.add(nivel);
                            }
                        }

                        Pattern pM = Pattern.compile("\\b(\\w*Modalidad\\w*)\\b");
                        for (String w : keys) {
                            if (pM.matcher(w).find()){
                                Modalidad modalidad = new Modalidad( object.get(w).getAsString(), docente);
                                modalidadList.add(modalidad);
                            }
                        }

                        asignatura = new Asignatura(object.get("AsignaturaId").getAsString(), object.get("Asignatura").getAsString(), docente, nivelList, modalidadList);

                        LocalizacionUsuario locUsuario = new LocalizacionUsuario(
                                object.get("LocalizacionId").getAsString(), object.get("Lat").getAsDouble(), object.get("Long").getAsDouble(), usuarioList.get(i)
                        );

                        localizacionList.add(locUsuario);

                        createMarkerAsigFromDB(locUsuario, docente);

                    }

                    if (usuarioList.size() > 0){
                        floatingActionMenu.setVisibility(View.VISIBLE);
                        initTooltip();
                    }
                }else if(response.errorBody() != null){
                    Toast.makeText(getActivity(), getResources().getString(R.string.search_asig_not_found), Toast.LENGTH_SHORT).show();
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

    public void createMarkerAsigFromDB(LocalizacionUsuario locUsuario, Docente docente){
        String title;

        LatLng location = new LatLng(locUsuario.getLat(), locUsuario.getLng());

        if (docente.getProfesion() != null){
            title = docente.getProfesion() + ":" + docente.getNombreCompleto();
        } else {
            title = docente.getNombreCompleto();
        }

        asigMarker = mMap.addMarker(new MarkerOptions()
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.teacher_icon))
                .position(location)
                .alpha(0.8f));

        asigMarker.setTag(locUsuario.getIdLocalizacionUsuario());

        list_markerAsig.add(asigMarker);
    }

    public void initTooltip(){

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");

        Tooltip tooltip = new Tooltip();
        tooltip.createTooltip(typeface,requireContext(),floatingActionMenu,getResources().getString(R.string.tooltipFiltrosMenu),0.45f,90,-300, getViewLifecycleOwner());
    }

    private void onMarkerLongClick(Marker asigMarker) {
        Log.d(TAG, "onMarkerLongClick");

        Docente docente = null;
        List<Modalidad> modalidads = new ArrayList<>();
        List<Nivel> nivels = new ArrayList<>();

        String idLoc = String.valueOf(asigMarker.getTag());

        for (LocalizacionUsuario loc: localizacionList){
            if (idLoc.equals(loc.getIdLocalizacionUsuario())){
                Usuario userMarker = loc.getUsuario();
                for (int i = 0; i < usuarioList.size(); i++){
                    if (userMarker.getIdUsuario().equals(usuarioList.get(i).getIdUsuario())){
                        docente = (Docente) usuarioList.get(i);
                    }
                }
            }
        }

        for (Modalidad m: modalidadList){
            if (m.getDocente() == docente){
                modalidads.add(m);
            }
        }

        for (Nivel n: nivelList){
            if (n.getDocente() == docente){
                nivels.add(n);
            }
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("docente", docente);
        bundle.putSerializable("asignatura", asignatura);
        bundle.putSerializable("modalidads", (Serializable) modalidads);
        bundle.putSerializable("nivels", (Serializable) nivels);

        FragmentManager fragmentManager = getFragmentManager();
        TeacherInfo dialogTeacherInfo = new TeacherInfo();
        dialogTeacherInfo.setArguments(bundle);
        dialogTeacherInfo.show(fragmentManager, "TeacherInfo");
    }

    public void initalLoginScreen() {
        cardInitalAddress.setVisibility(View.GONE);
        search_box.setVisibility(View.VISIBLE);
    }

    public void clearData(){
        clearMapMarker();
        usuarioList = new ArrayList<>();
        localizacionList = new ArrayList<>();
        nivelList = new ArrayList<>();
        modalidadList = new ArrayList<>();
        asignatura = null;
    }

    public void clearMapMarker() {
        for (int i = 0; i < list_markerAsig.size(); i++ ) {
            asigMarker = list_markerAsig.get(i);
            asigMarker.remove();
        }
        list_markerAsig = new ArrayList<>();
    }

    @Override
    public void OnSetLevMod(List<String> nivelSeleccionado, List<String> modalidadSeleccionado) {
        nivSelec = nivelSeleccionado;
        modSelec = modalidadSeleccionado;
        constListSetFiltered.add(ConstantFiltro.NIVMOD);
        setFilter();
    }

    private void searchLevMod(List<String> nivelSeleccionado, List<String> modalidadSeleccionado) {
        List<Docente> queryNivelDoc = new ArrayList<>();
        List<Docente> queryModalidadDoc = new ArrayList<>();
        List<Docente> queryDoc = new ArrayList<>();
        List<LocalizacionUsuario> locRemove = new ArrayList<>();

        if (nivelSeleccionado.size() > 0){
            for (String nivSel: nivelSeleccionado){
                for (Nivel nivList: nivelList){
                    if (nivList.getNombre().equals(nivSel)){
                        if (!queryNivelDoc.contains(nivList.getDocente())){
                            queryNivelDoc.add(nivList.getDocente());
                        }
                    }
                }
            }
        }

        if (modalidadSeleccionado.size() > 0){
            for (String modSel: modalidadSeleccionado){
                for (Modalidad modList: modalidadList){
                    if (modList.getNombre().equals(modSel)){
                        if (!queryModalidadDoc.contains(modList.getDocente())){
                            queryModalidadDoc.add(modList.getDocente());
                        }
                    }
                }
            }
        }

        if (queryNivelDoc.size() > 0 && queryModalidadDoc.size() > 0){
            for (Docente nqd: queryNivelDoc){
                for (Docente mqd: queryModalidadDoc){
                    if (nqd.getIdDocente().equals(mqd.getIdDocente())){
                        if (!queryDoc.contains(nqd)){
                            queryDoc.add(nqd);
                        }
                    }
                }
            }
        }else if (queryNivelDoc.size() > 0 && queryModalidadDoc.isEmpty()){
            queryDoc.addAll(queryNivelDoc);
        }else {
            queryDoc.addAll(queryModalidadDoc);
        }

        List<Usuario> usuarioInLocalizacion = new ArrayList<>();
        for (LocalizacionUsuario locUsu: localizacionListFiltered){
            usuarioInLocalizacion.add(locUsu.getUsuario());
        }

        List<Usuario> usuarioQuery = new ArrayList<>();
        for (Docente doc: queryDoc){
            Usuario usr = (Usuario) doc;
            usuarioQuery.add(usr);
        }

        for (Usuario u: usuarioInLocalizacion){
            if (!usuarioQuery.contains(u)){
                for (LocalizacionUsuario locUsu: localizacionListFiltered){
                    if (locUsu.getUsuario().equals(u)){
                        locRemove.add(locUsu);
                    }
                }
            }
        }

        for (Marker asig: list_markerAsig){
            asig.remove();
        }

        list_markerAsig.clear();
        localizacionListFiltered.removeAll(locRemove);
        for (LocalizacionUsuario locUsr: localizacionListFiltered){
            for (Docente doc: queryDoc){
                if (locUsr.getUsuario().getIdUsuario().equals(doc.getIdUsuario())){
                    createMarkerAsigFromDB(locUsr, doc);
                }
            }
        }

        floatingActionMenu.close(true);

        floatingActionNivMod.setImageResource(R.drawable.ic_remov_not);
        floatingActionNivMod.setLabelText(getResources().getString(R.string.fab_erase_fil)+" "+ ConstantFiltro.NIVMOD.toString());
        NivModFilter = true;
    }

    @Override
    public void OnSetCosto(int costoInicial, int costoFinal) {
        costoIni = costoInicial;
        costoFin = costoFinal;
        constListSetFiltered.add(ConstantFiltro.COSTO);
        setFilter();
    }

    private void searchCosto(String nombAsignatura, int costoIni, int costoFin) {
        mEstudianteService.setFilterCosto(nombAsignatura,costoIni,costoFin).enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                if(response.isSuccessful()) {

                    JsonObject object = new JsonObject();

                    List<String> LocId = new ArrayList<>();

                    for (int i = 0; i < response.body().size(); i++){

                        object = response.body().get(i);

                        String locUsuario = object.get("LocUsuario").getAsString();
                        if (LocId.isEmpty() || !LocId.contains(locUsuario)){
                            LocId.add(locUsuario);
                        }
                    }

                    updateMarkerByFilter(LocId, ConstantFiltro.COSTO);

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

    private void searchHorario(String nombAsignatura, Date dateStartTime, Date dateEndTime, String sender) {
        mEstudianteService.setFilterHorario(nombAsignatura, formatDate.formatDateToDB(dateStartTime), formatDate.formatDateToDB(dateEndTime)).enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {

                if(response.isSuccessful()) {

                    JsonObject object = new JsonObject();

                    List<String> LocId = new ArrayList<>();

                    for (int i = 0; i < response.body().size(); i++){

                        object = response.body().get(i);

                        String locUsuario = object.get("LocUsuario").getAsString();
                        if (LocId.isEmpty() || !LocId.contains(locUsuario)){
                            LocId.add(locUsuario);
                        }
                    }

                    updateMarkerByFilter(LocId, sender);

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

    private void updateMarkerByFilter(List<String> locId, String sender) {
        List<LocalizacionUsuario> locRemove = new ArrayList<>();

        List<LocalizacionUsuario> userLocalization = new ArrayList<>();
        for (LocalizacionUsuario locUsu: localizacionListFiltered){
            for (String l: locId){
                if (locUsu.getIdLocalizacionUsuario().equals(l)){
                    userLocalization.add(locUsu);
                }
            }
        }

        for (LocalizacionUsuario locUsu: localizacionListFiltered){
            if (!userLocalization.contains(locUsu)){
                locRemove.add(locUsu);
            }
        }

        for (Marker asig: list_markerAsig){
            asig.remove();
        }

        list_markerAsig.clear();
        localizacionListFiltered.removeAll(locRemove);
        for (LocalizacionUsuario locUsr: localizacionListFiltered){
            for (Usuario usr: usuarioList){
                if (locUsr.getUsuario().getIdUsuario().equals(usr.getIdUsuario())){
                    Docente doc = (Docente) usr;
                    createMarkerAsigFromDB(locUsr, doc);
                }
            }
        }

        floatingActionMenu.close(true);


        if (sender.equals(ConstantFiltro.COSTO)){
            CostoFilter = true;
            floatingActionCosto.setImageResource(R.drawable.ic_remov_not);
            floatingActionCosto.setLabelText(getResources().getString(R.string.fab_erase_fil) +" "+ ConstantFiltro.COSTO.toString());
        }else if (sender.equals(ConstantFiltro.HORARIO)){
            HorarioFilter = true;
            floatingActionHorario.setImageResource(R.drawable.ic_remov_not);
            floatingActionHorario.setLabelText(getResources().getString(R.string.fab_erase_fil) +" "+ ConstantFiltro.HORARIO.toString());
        }else if (sender.equals(ConstantFiltro.FECHA)){
            FechaFilter = true;
            floatingActionFecha.setImageResource(R.drawable.ic_remov_not);
            floatingActionFecha.setLabelText(getResources().getString(R.string.fab_erase_fil) +" "+ ConstantFiltro.FECHA.toString());
        }

    }

    private void setFilter(){
        if (!InitFilter){
            if (!localizacionListFiltered.isEmpty()) {
                localizacionListFiltered.clear();
            }
            localizacionListFiltered.addAll(localizacionList);
            InitFilter = true;
        }

        if (!constListSetFiltered.isEmpty()){
            for (String filter: constListSetFiltered){
                switch (filter) {
                    case ConstantFiltro.NIVMOD:
                        searchLevMod(nivSelec,modSelec);
                        break;
                    case ConstantFiltro.COSTO:
                        searchCosto(asignatura.getNombre(),costoIni,costoFin);
                        break;
                    case ConstantFiltro.HORARIO:
                        searchHorario(asignatura.getNombre(),dateStartTime,dateEndTime, filter);
                        break;
                    case ConstantFiltro.FECHA:
                        searchHorario(asignatura.getNombre(),selectedDateStart,selectedDateEnd, filter);
                        if (HorarioFilter) {
                            constListSetFiltered.add(ConstantFiltro.HORARIO);
                        }
                        break;
                    case ConstantFiltro.UBICACION:
                        break;
                    default:
                        break;
                }
            }
        } else {
            for (LocalizacionUsuario locUsr: localizacionList){
                for (Usuario usr: usuarioList){
                    if (locUsr.getUsuario().getIdUsuario().equals(usr.getIdUsuario())){
                        Docente doc = (Docente) usr;
                        createMarkerAsigFromDB(locUsr, doc);
                    }
                }
            }
        }
    }

    private void clearFilter(String remitente) {
        InitFilter = false;

        switch (remitente) {
            case ConstantFiltro.NIVMOD:
                NivModFilter = false;
                constListSetFiltered.remove(ConstantFiltro.NIVMOD);
                floatingActionNivMod.setImageResource(R.drawable.ic_education);
                floatingActionNivMod.setLabelText(getResources().getString(R.string.fab_modalidad_nivel));
                floatingActionMenu.close(true);
                setFilter();
                break;
            case ConstantFiltro.COSTO:
                CostoFilter = false;
                constListSetFiltered.remove(ConstantFiltro.COSTO);
                floatingActionCosto.setImageResource(R.drawable.ic_money);
                floatingActionCosto.setLabelText(getResources().getString(R.string.fab_costo));
                floatingActionMenu.close(true);
                setFilter();
                break;
            case ConstantFiltro.HORARIO:
                HorarioFilter = false;
                dateStartTime = null;
                dateEndTime = null;
                constListSetFiltered.remove(ConstantFiltro.HORARIO);
                floatingActionHorario.setImageResource(R.drawable.ic_watch);
                floatingActionHorario.setLabelText(getResources().getString(R.string.fab_horario));
                floatingActionMenu.close(true);
                setFilter();
                break;
            case ConstantFiltro.FECHA:
                FechaFilter = false;
                constListSetFiltered.remove(ConstantFiltro.FECHA);
                floatingActionFecha.setImageResource(R.drawable.ic_calendar);
                floatingActionFecha.setLabelText(getResources().getString(R.string.fab_calendar));
                floatingActionMenu.close(true);
                setFilter();
                break;
            case ConstantFiltro.UBICACION:
                UbiacionFilter = false;
                constListSetFiltered.remove(ConstantFiltro.UBICACION);
                FloatingActionUbicacion.setImageResource(R.drawable.ic_location_b);
                FloatingActionUbicacion.setLabelText(getResources().getString(R.string.fab_ubicacion));
                updateLocation(false);
                floatingActionMenu.close(true);
                setFilter();
                break;
            default:
                break;
        }

    }

    @Override
    public void OnSetTimePicker(int startHour , int endMinute, int startFinalHour, int endFinalMinute) {
        Calendar localTime = Calendar.getInstance();

        if (selectedDateStart == null){
            dateStartTime = formatDate.getDateCalendar(localTime,startHour,endMinute);
        } else {
            localTime.setTime(selectedDateStart);
            dateStartTime = formatDate.getDateCalendar(localTime,startHour,endMinute);
        }

        if (selectedDateEnd == null){
            dateEndTime = formatDate.getDateCalendar(localTime,startFinalHour,endFinalMinute);
        } else {
            localTime.setTime(selectedDateEnd);
            dateEndTime = formatDate.getDateCalendar(localTime,startFinalHour,endFinalMinute);
        }

        constListSetFiltered.add(ConstantFiltro.HORARIO);
        setFilter();
    }

    public void OnSetDatePicker(Pair<Long, Long> selection) {
        selectedDateStart = new Date(selection.first.longValue());

        Calendar cal = Calendar.getInstance();
        Calendar calTime = Calendar.getInstance();

        cal.setTime(selectedDateStart);
        if (dateStartTime == null){
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
        } else {
            calTime.setTime(dateStartTime);
            cal.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
        }

        selectedDateStart = cal.getTime();

        selectedDateEnd = new Date(selection.second.longValue());
        cal.setTime(selectedDateEnd);
        if (dateEndTime == null){
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
        } else {
            calTime.setTime(dateEndTime);
            cal.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
        }

        selectedDateEnd = cal.getTime();

        constListSetFiltered.add(ConstantFiltro.FECHA);

        if (HorarioFilter){
            InitFilter = false;
            constListSetFiltered.remove(ConstantFiltro.HORARIO);
        }
        setFilter();
    }

    private void setUbicacionFilter() {
        final FlatDialog flatDialog = new FlatDialog(requireContext());
        flatDialog.setTitle(getResources().getString(R.string.title_filtro_ubicacion))
                .setTitleColor(getResources().getColor(R.color.item_name))
                .setBackgroundColor(getResources().getColor(R.color.bg_white))
                .setSubtitle(getResources().getString(R.string.subtitle_filtro_ubicacion))
                .setSubtitleColor(getResources().getColor(R.color.item_name))
                .setFirstTextFieldHint(getResources().getString(R.string.filtro_ubicacion_hint))
                .setFirstTextFieldHintColor(getResources().getColor(R.color.colorPrimaryDarkOne))
                .setFirstTextFieldBorderColor(getResources().getColor(R.color.colorPrimaryDarkOne))
                .setFirstButtonText(getResources().getString(R.string.button_accept_cont))
                .setSecondButtonText(getResources().getString(R.string.button_cancel_cont))
                .setFirstTextFieldTextColor(getResources().getColor(R.color.colorPrimaryDarkOne))
                .withFirstButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(requireContext(), flatDialog.getFirstTextField(), Toast.LENGTH_SHORT).show();
                        try {
                            setUbicacion(flatDialog.getFirstTextField());
                            flatDialog.dismiss();
                            floatingActionMenu.close(true);
                            UbiacionFilter = true;
                            FloatingActionUbicacion.setImageResource(R.drawable.ic_remov_not);
                            FloatingActionUbicacion.setLabelText(getResources().getString(R.string.fab_erase_fil) +" "+ ConstantFiltro.UBICACION.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .withSecondButtonListner(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flatDialog.dismiss();
                    }
                })
                .show();
    }

    public void setUbicacion(String selectedDireccion) throws IOException {

        Geocoder coder = new Geocoder(getActivity());

        if (!selectedDireccion.equals("")){
            List<Address> address = coder.getFromLocationName(selectedDireccion, 1);
            Address locationA = address.get(0);
            locationFilter = new LatLng(locationA.getLatitude(), locationA.getLongitude());
            updateLocation(true);
        }

    }
}
