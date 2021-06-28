package com.ejemplo.appdocente.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ejemplo.appdocente.DTO.Asignatura;
import com.ejemplo.appdocente.DTO.Docente;
import com.ejemplo.appdocente.DTO.Modalidad;
import com.ejemplo.appdocente.DTO.Nivel;
import com.ejemplo.appdocente.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class TeacherInfo extends DialogFragment {

    private View viewInit = null;
    private TextView txtNameTeacher, txtWorkTeacher;
    private EditText editUsername, editModalidades, editNiveles, editInformacion, editLocalizacion;
    private Button btnCont, btnCerrar;

    private Docente docente = null;
    private Asignatura asignatura = null;
    private List<Modalidad> modalidads = new ArrayList<>();
    private List<Nivel> nivels = new ArrayList<>();
    private StringBuilder sb;


    public TeacherInfo() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        viewInit = inflater.inflate(R.layout.dialog_info_asigteach, null);

        return infoTeacher(viewInit);
    }

    @SuppressLint("ClickableViewAccessibility")
    public AlertDialog infoTeacher(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        txtNameTeacher = v.findViewById(R.id.text_name_teacher_field);
        txtWorkTeacher = v.findViewById(R.id.text_work_teacher_field);
        editUsername = v.findViewById(R.id.edit_username);
        editModalidades = v.findViewById(R.id.edit_mod);
        editNiveles = v.findViewById(R.id.edit_nivel);
        editInformacion = v.findViewById(R.id.edit_info);
        editLocalizacion = v.findViewById(R.id.edit_location);

        btnCont = v.findViewById(R.id.button_accept);
        btnCerrar = v.findViewById(R.id.button_cancel);

        getBundle();
        
        txtNameTeacher.setText(docente.getNombreCompleto());
        txtWorkTeacher.setText(docente.getProfesion());
        editUsername.setText(docente.getUsername());
        editInformacion.setText(docente.getInfo());
        editLocalizacion.setText(docente.getCiudad() + " / " + docente.getPais());

        if (modalidads.size() > 0){
            List<String> txtNameModalidades = new ArrayList<>();
            for (Modalidad m: modalidads){
                txtNameModalidades.add(m.getNombre());
            }

            sb = new StringBuilder();
            for (String s : txtNameModalidades)
            {
                sb.append(s);
                sb.append("\n");
            }

            editModalidades.setText(sb);
        }else{
            editModalidades.setText("");
        }

        if (nivels.size() > 0){
            List<String> txtNameNiveles = new ArrayList<>();
            for (Nivel n: nivels){
                txtNameNiveles.add(n.getNombre());
            }

            sb = new StringBuilder();
            for (String s : txtNameNiveles)
            {
                sb.append(s);
                sb.append("\n");
            }

            editNiveles.setText(sb);
        }else{
            editNiveles.setText("");
        }

        //Buttons
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("docente", docente);
                bundle.putSerializable("modalidads", (Serializable) modalidads);
                bundle.putSerializable("nivels", (Serializable) nivels);
                bundle.putSerializable("asignatura", asignatura);

                ContactTeacher dialogContactTeacher = new ContactTeacher();
                dialogContactTeacher.setArguments(bundle);

                dialogContactTeacher.show(getChildFragmentManager(), "ContactTeacher");
            }
        });

        builder.setView(v);

        return builder.create();
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

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
