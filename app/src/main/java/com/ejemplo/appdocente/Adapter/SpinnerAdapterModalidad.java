package com.ejemplo.appdocente.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ejemplo.appdocente.DTO.Modalidad;
import com.ejemplo.appdocente.R;

import java.util.List;

public class SpinnerAdapterModalidad extends ArrayAdapter<Modalidad> {

    private Context context;
    private List<Modalidad> values;

    public SpinnerAdapterModalidad(Context context,
                               List<Modalidad> values) {
        super(context, 0, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return super.getCount()-1; // you dont display last item. It is used as hint.
    }

    @Override
    public Modalidad getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_drop_down_calendar, parent, false);
        }

        TextView label = convertView.findViewById(R.id.txtDropDownCalendarFirst);
        TextView labelFechaFin = convertView.findViewById(R.id.txtDropDownCalendarSecond);
        ImageView image = convertView.findViewById(R.id.imgDropDownMenuIcon);

        labelFechaFin.setVisibility(View.GONE);
        image.setImageResource(R.drawable.classroom);

        label.setTextColor(Color.BLACK);
        label.setText(values.get(position).getNombre());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getView(position,convertView,parent);
    }

}
