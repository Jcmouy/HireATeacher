package com.ejemplo.appdocente.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ejemplo.appdocente.Constants.ConstantModalidad;
import com.ejemplo.appdocente.Constants.ConstantNivel;
import com.ejemplo.appdocente.R;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class FilerLevMod extends DialogFragment {

    private View viewInit = null;
    private Button btnCont, btnCerrar;

    List<String> nivelSeleccionado;
    List<String> modalidadSeleccionado;

    FilterLevModInterface mListener;
    public interface FilterLevModInterface {
        void OnSetLevMod(List<String> nivelSeleccionado, List<String> modalidadSeleccionado);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        viewInit = inflater.inflate(R.layout.filter_search_levmod, null);
        mListener = (FilterLevModInterface) getParentFragment();
        return filterLevMod(viewInit);
    }

    @SuppressLint("ClickableViewAccessibility")
    public AlertDialog filterLevMod(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        nivelSeleccionado = new ArrayList<>();
        modalidadSeleccionado = new ArrayList<>();

        final CustomCheckBox scbPrimaria = (CustomCheckBox) v.findViewById(R.id.check_filtroPrimaria);
        final CustomCheckBox scbSecundaria = (CustomCheckBox) v.findViewById(R.id.check_filtroSecundaria);
        final CustomCheckBox scbTerciaria = (CustomCheckBox) v.findViewById(R.id.check_filtroTerciaria);
        final CustomCheckBox scbUniveristaria = (CustomCheckBox) v.findViewById(R.id.check_filtroUniversitaria);

        final CustomCheckBox scbOnline = (CustomCheckBox) v.findViewById(R.id.check_filtroOnline);
        final CustomCheckBox scbPresencial = (CustomCheckBox) v.findViewById(R.id.check_filtroPresencial);
        final CustomCheckBox scbSemiPresencial = (CustomCheckBox) v.findViewById(R.id.check_filtroSemiPresencial);
        final CustomCheckBox scbDomicilio = (CustomCheckBox) v.findViewById(R.id.check_filtroDomicilio);

        btnCont = v.findViewById(R.id.button_filterCosto_accept);
        btnCerrar = v.findViewById(R.id.button_filterCosto_cancel);

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

                if (scbPrimaria.isChecked()){
                    nivelSeleccionado.add(ConstantNivel.PRIMARIA);
                }
                if (scbSecundaria.isChecked()){
                    nivelSeleccionado.add(ConstantNivel.SECUNDARIA);
                }
                if (scbTerciaria.isChecked()){
                    nivelSeleccionado.add(ConstantNivel.TERCIARIA);
                }
                if (scbUniveristaria.isChecked()){
                    nivelSeleccionado.add(ConstantNivel.UNIVERSITARIA);
                }

                if (scbOnline.isChecked()){
                    modalidadSeleccionado.add(ConstantModalidad.ONLINE);
                }
                if (scbPresencial.isChecked()){
                    modalidadSeleccionado.add(ConstantModalidad.PRESENCIAL);
                }
                if (scbSemiPresencial.isChecked()){
                    modalidadSeleccionado.add(ConstantModalidad.SEMIPRESENCIAL);
                }
                if (scbDomicilio.isChecked()){
                    modalidadSeleccionado.add(ConstantModalidad.DOMICILIO);
                }

                if (nivelSeleccionado.size() > 0 || modalidadSeleccionado.size() > 0){
                    if(mListener!=null)
                        mListener.OnSetLevMod(nivelSeleccionado, modalidadSeleccionado);
                    dismiss();
                }else {
                    dismiss();
                }

            }
        });

        builder.setView(v);

        return builder.create();
    }

}
