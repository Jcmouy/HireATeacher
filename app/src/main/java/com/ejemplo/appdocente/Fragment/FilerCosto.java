package com.ejemplo.appdocente.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ejemplo.appdocente.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import it.sephiroth.android.library.rangeseekbar.RangeSeekBar;

public class FilerCosto extends DialogFragment {

    private View viewInit = null;
    private Button btnCont, btnCerrar;
    private RangeSeekBar seekBar;
    private TextView textseek;

    FilterCostoInterface mListener;
    public interface FilterCostoInterface {
        void OnSetCosto(int costoInicial, int costoFinal);
    }

    public FilerCosto() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        viewInit = inflater.inflate(R.layout.filter_search_money, null);
        mListener = (FilterCostoInterface) getParentFragment();
        return filterCosto(viewInit);
    }

    @SuppressLint("ClickableViewAccessibility")
    public AlertDialog filterCosto(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        seekBar = v.findViewById(R.id.rangeSeekBar);
        textseek = v.findViewById(R.id.textView2);
        btnCont = v.findViewById(R.id.button_filterCosto_accept);
        btnCerrar = v.findViewById(R.id.button_filterCosto_cancel);

        textseek.setText("$" +seekBar.getProgressStart() + " - " + "$" +seekBar.getProgressEnd());

        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(
                    final RangeSeekBar seekBar, final int progressStart, final int progressEnd, final boolean fromUser) {
                    updateRangeText(textseek, seekBar);
            }

            @Override
            public void onStartTrackingTouch(final RangeSeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(final RangeSeekBar seekBar) { }
        });

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
                if(mListener!=null)
                    mListener.OnSetCosto(seekBar.getProgressStart(), seekBar.getProgressEnd());
                dismiss();
            }
        });


        builder.setView(v);

        return builder.create();
    }

    private void updateRangeText(TextView textseek, RangeSeekBar seekBar) {
        textseek.setText("$" +seekBar.getProgressStart() + " - " + "$" +seekBar.getProgressEnd());
    }
}
