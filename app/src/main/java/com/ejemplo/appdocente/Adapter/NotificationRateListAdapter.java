package com.ejemplo.appdocente.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ejemplo.appdocente.Constants.ConstantTipoUsuario;
import com.ejemplo.appdocente.Controller.NotificationRateController;
import com.ejemplo.appdocente.DTO.NotificationRate;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.R;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NotificationRateListAdapter extends RecyclerView.Adapter<NotificationRateListAdapter.MyViewHolder> {
    private Context context;
    private PrefManager pref;
    private List<NotificationRate> notificacionRateList;
    private NotificationRateController notifRateCont;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, feedback_usu, name_tit, feedback_usu_tit;
        public RelativeLayout viewBackground, viewForeground;
        public RatingBar ratingBar;
        public ScaleRatingBar ratingScaleBar;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            feedback_usu = view.findViewById(R.id.feedback_usu);
            name_tit = view.findViewById(R.id.name_tit);
            feedback_usu_tit = view.findViewById(R.id.feedback_usu_tit);
            pref = new PrefManager(context);
            ratingScaleBar = view.findViewById(R.id.simpleRatingBar);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            notifRateCont = new NotificationRateController(context);
        }
    }

    public NotificationRateListAdapter(Context context, List<NotificationRate> notificacionRateList) {
        this.context = context;
        this.notificacionRateList = notificacionRateList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_rate_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final NotificationRate item = notificacionRateList.get(position);
        if (pref.getTypeUser().equals(ConstantTipoUsuario.ESTUDIANTE)) {
            holder.name_tit.setText( ConstantTipoUsuario.DOCENTE + ":");
        } else {
            holder.name_tit.setText( ConstantTipoUsuario.ESTUDIANTE + ":");
        }
        holder.name.setText(item.getNombreUsuarioValorado());
        if (!item.getFeedback().isEmpty()){
            holder.feedback_usu_tit.setText(context.getApplicationContext().getResources().getString(R.string.rate_list_adapter_feedback));
            holder.feedback_usu.setText(item.getFeedback());
        }
        holder.ratingScaleBar.setRating(item.getFeedbackVal());

        Glide.with(context);
    }

    @Override
    public int getItemCount() {
        return notificacionRateList.size();
    }

    public void removeItem(NotificationRate item, int position) {
        notificacionRateList.remove(position);
        notifRateCont.deleteNotificacion(item);
        notifyItemRemoved(position);
    }

    public void restoreItem(NotificationRate item, int position) {
        notificacionRateList.add(position, item);
        notifRateCont.saveNotificacionDB(item);
        notifyItemInserted(position);
    }
}
