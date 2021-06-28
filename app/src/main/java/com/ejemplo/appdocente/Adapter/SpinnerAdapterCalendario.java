package com.ejemplo.appdocente.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ejemplo.appdocente.DTO.Calendario;
import com.ejemplo.appdocente.R;
import com.ejemplo.appdocente.Util.FormatDate;

import java.util.List;

public class SpinnerAdapterCalendario extends ArrayAdapter<Calendario> {

    private Context context;
    private List<Calendario> values;
    private FormatDate formatDate = new FormatDate();

    public SpinnerAdapterCalendario(Context context, List<Calendario> values) {
        super(context,0, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public Calendario getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_drop_down_calendar, parent, false);
        }

        TextView labelFechaInicio = convertView.findViewById(R.id.txtDropDownCalendarFirst);
        TextView labelFechaFin = convertView.findViewById(R.id.txtDropDownCalendarSecond);

        labelFechaInicio.setTextColor(Color.BLACK);
        labelFechaFin.setTextColor(Color.BLACK);

        labelFechaInicio.setText(formatDate.dateToString(values.get(position).getFechaInicio()) + "    -    ");
        labelFechaFin.setText(formatDate.dateToString(values.get(position).getFechaFin()));

        return convertView;
    }

    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
