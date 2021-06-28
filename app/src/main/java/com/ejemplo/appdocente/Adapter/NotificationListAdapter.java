package com.ejemplo.appdocente.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ejemplo.appdocente.Controller.NotificationController;
import com.ejemplo.appdocente.DTO.Notificacion;
import com.ejemplo.appdocente.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyViewHolder> {
    private Context context;
    private List<Notificacion> notificacionList;
    private NotificationController notifCont;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, price;
        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            thumbnail = view.findViewById(R.id.thumbnail);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            notifCont = new NotificationController(context);
        }
    }


    public NotificationListAdapter(Context context, List<Notificacion> notificacionList) {
        this.context = context;
        this.notificacionList = notificacionList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Notificacion item = notificacionList.get(position);
        holder.name.setText(item.getMensaje());
        item.setThumbnail(R.drawable.notificationb);

        Glide.with(context)
                .load(item.getThumbnail())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return notificacionList.size();
    }

    public void removeItem(Notificacion item, int position) {
        notificacionList.remove(position);
        notifCont.deleteNotificacion(item);
        notifyItemRemoved(position);
    }

    public void restoreItem(Notificacion item, int position) {
        notificacionList.add(position, item);
        notifCont.saveNotificacionDB(item);
        notifyItemInserted(position);
    }
}
