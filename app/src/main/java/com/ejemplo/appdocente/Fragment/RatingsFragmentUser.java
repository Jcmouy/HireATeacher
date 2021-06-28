package com.ejemplo.appdocente.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ejemplo.appdocente.Adapter.NotificationRateListAdapter;
import com.ejemplo.appdocente.Constants.ConstantTipoUsuario;
import com.ejemplo.appdocente.Controller.NotificationRateController;
import com.ejemplo.appdocente.DTO.NotificationRate;
import com.ejemplo.appdocente.Helper.RecyclerItemTouchHelperNotificationRat;
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


public class RatingsFragmentUser extends Fragment implements RecyclerItemTouchHelperNotificationRat.RecyclerItemTouchHelperListener {

    private List<NotificationRate> last_notificacionsRate;
    private RecyclerView recyclerView;
    private NotificationRateListAdapter mAdapter;
    private FrameLayout coordinatorLayout;
    private NotificationRateController notifRateCont;

    public RatingsFragmentUser() {
    }

    public static RatingsFragmentUser newInstance(String param1, String param2) {
        RatingsFragmentUser fragment = new RatingsFragmentUser();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        last_notificacionsRate = new ArrayList<>();
        notifRateCont = new NotificationRateController(requireContext());
        last_notificacionsRate = notifRateCont.loadAllNotificacionesByTipoUsuario(ConstantTipoUsuario.ESTUDIANTE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_user, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_rating);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout_rating);
        mAdapter = new NotificationRateListAdapter(requireContext(), last_notificacionsRate);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperNotificationRat(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationRateListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = last_notificacionsRate.get(viewHolder.getAdapterPosition()).getMensaje();

            // backup of removed item for undo purpose
            final NotificationRate deletedItem = last_notificacionsRate.get(viewHolder.getAdapterPosition());
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

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.notifyDataSetChanged();
    }
}
