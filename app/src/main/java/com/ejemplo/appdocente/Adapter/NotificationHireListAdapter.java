package com.ejemplo.appdocente.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ejemplo.appdocente.Controller.NotificationHireController;
import com.ejemplo.appdocente.DTO.NotificationHire;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NotificationHireListAdapter extends RecyclerView.Adapter<NotificationHireListAdapter.MyViewHolder> {
    private Context context;
    private PrefManager pref;
    private List<NotificationHire> notificacionHireList;
    private NotificationHireController notifHireCont;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView student, mod, asig, hor, fech, money;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            student = view.findViewById(R.id.name);
            mod = view.findViewById(R.id.mod);
            asig = view.findViewById(R.id.asig);
            hor = view.findViewById(R.id.hor);
            fech = view.findViewById(R.id.fech);
            money = view.findViewById(R.id.money_earn);
            pref = new PrefManager(context);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            notifHireCont = new NotificationHireController(context);
        }
    }

    public NotificationHireListAdapter(Context context, List<NotificationHire> notificacionHireList) {
        this.context = context;
        this.notificacionHireList = notificacionHireList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_dash_teacher_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final NotificationHire item = notificacionHireList.get(position);
        holder.student.setText(item.getNombreEstudiante());
        holder.mod.setText(item.getNombreModalidad());
        holder.asig.setText(item.getNombreAsignatura());
        holder.hor.setText(item.getHorarioClase());
        holder.fech.setText(item.getFechaClase());
        holder.money.setText("$"+item.getCostoEstablecido());

        Glide.with(context);
    }

    @Override
    public int getItemCount() {
        return notificacionHireList.size();
    }

    public void removeItem(NotificationHire item, int position) {
        notificacionHireList.remove(position);
        notifHireCont.deleteNotificacion(item);
        notifyItemRemoved(position);
    }

    public void restoreItem(NotificationHire item, int position) {
        notificacionHireList.add(position, item);
        notifHireCont.saveNotificacionDB(item);
        notifyItemInserted(position);
    }
}
