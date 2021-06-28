package com.ejemplo.appdocente.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ejemplo.appdocente.Adapter.NotificationListAdapter;
import com.ejemplo.appdocente.Controller.NotificationController;
import com.ejemplo.appdocente.DTO.Notificacion;
import com.ejemplo.appdocente.Helper.RecyclerItemTouchHelperNotificationCont;
import com.ejemplo.appdocente.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationFragment extends Fragment implements RecyclerItemTouchHelperNotificationCont.RecyclerItemTouchHelperListener {

    private List<Notificacion> notificacions;
    private List<Notificacion> last_notificacions;
    private RecyclerView recyclerView;
    private NotificationListAdapter mAdapter;
    private FrameLayout coordinatorLayout;
    private NotificationController notifCont;

    public NotificationFragment() {
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificacions = new ArrayList<>();
        //last_notificacions = new ArrayList<>();
        notifCont = new NotificationController(requireContext());
        //last_notificacions = notifCont.loadAllNotificaciones();
        notificacions = notifCont.loadAllNotificaciones();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // getBundle();

        recyclerView = view.findViewById(R.id.recycler_view);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        mAdapter = new NotificationListAdapter(requireContext(), notificacions);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperNotificationCont(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return view;
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null){
            notificacions = (List<Notificacion>) bundle.getSerializable("notificacions");
            notifCont.saveNotificacionesDB(notificacions);
        } else if (notificacions.size() > 0 && last_notificacions.size() > 0){
            notifCont.saveNotificacionesDB(notificacions);
            notificacions.addAll(last_notificacions);
        } else if (notificacions.size() == 0 && last_notificacions.size() > 0) {
            notificacions.addAll(last_notificacions);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = notificacions.get(viewHolder.getAdapterPosition()).getMensaje();

            // backup of removed item for undo purpose
            final Notificacion deletedItem = notificacions.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(deletedItem, viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " " + getResources().getString(R.string.snack_delete_dash), Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
