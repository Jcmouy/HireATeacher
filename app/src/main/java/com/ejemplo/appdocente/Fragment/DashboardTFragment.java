package com.ejemplo.appdocente.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ejemplo.appdocente.Adapter.NotificationHireListAdapter;
import com.ejemplo.appdocente.Controller.NotificationHireController;
import com.ejemplo.appdocente.DTO.NotificationHire;
import com.ejemplo.appdocente.Helper.PrefManager;
import com.ejemplo.appdocente.Helper.RecyclerItemTouchHelperNotificationHire;
import com.ejemplo.appdocente.R;
import com.ejemplo.appdocente.Util.UBubbleShowCase;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class DashboardTFragment extends Fragment implements RecyclerItemTouchHelperNotificationHire.RecyclerItemTouchHelperListener, SwipeRefreshLayout.OnRefreshListener  {

    private List<NotificationHire> last_notificacionsHire;
    private RecyclerView recyclerView;
    private NotificationHireListAdapter mAdapter;
    private FrameLayout coordinatorLayout;
    private NotificationHireController notifHireCont;
    private SwipeRefreshLayout swipLayout;
    private View view;
    private PrefManager pref;

    public DashboardTFragment() {
    }

    public static DashboardTFragment newInstance(String param1, String param2) {
        DashboardTFragment fragment = new DashboardTFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        last_notificacionsHire = new ArrayList<>();
        notifHireCont = new NotificationHireController(requireContext());
        pref = new PrefManager(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dash_teacher, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_dash_teacher);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout_dash_teacher);
        swipLayout = view.findViewById(R.id.swipe_layout);
        swipLayout.setOnRefreshListener(this);

        setNotification();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperNotificationHire(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        if (pref.isFirstTimeLaunch()) {
            UBubbleShowCase uBubbleShowCase =  new UBubbleShowCase(requireContext(), new WeakReference<FragmentActivity>(requireActivity()), view);
            uBubbleShowCase.startShowCase();
            pref.setFirstTimeLaunch(false);
        }

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotificationHireListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = last_notificacionsHire.get(viewHolder.getAdapterPosition()).getMensaje();

            // backup of removed item for undo purpose
            final NotificationHire deletedItem = last_notificacionsHire.get(viewHolder.getAdapterPosition());
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

    @Override
    public void onRefresh() {
        setNotification();
        swipLayout.setRefreshing(false);
    }

    private void setNotification() {
        last_notificacionsHire = notifHireCont.loadAllNotificaciones();
        mAdapter = new NotificationHireListAdapter(requireContext(), last_notificacionsHire);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }
}
