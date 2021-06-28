package com.ejemplo.appdocente.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ejemplo.appdocente.R;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class RatingsFragment extends Fragment {

    private JellyToggleButton jellyToggleButton;
    private String RAT_FRAG_USER = "ratFragUser";
    private String RAT_FRAG_TEACHER = "ratFragTeacher";
    private FragmentManager fragmentManager;
    private RatingsFragmentUser ratingsFragmentUser = new RatingsFragmentUser();
    private RatingsFragmentTeacher ratingsFragmentTeacher = new RatingsFragmentTeacher();

    public RatingsFragment() {
    }

    public static RatingsFragment newInstance(String param1, String param2) {
        RatingsFragment fragment = new RatingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ratings_parent, container, false);

        jellyToggleButton = view.findViewById(R.id.jelly_toggle);

        setInitalFrameRating();

        jellyToggleButton.setOnStateChangeListener(new JellyToggleButton.OnStateChangeListener() {
            @Override
            public void onStateChange(float process, State state, JellyToggleButton jtb) {
                stateChange(process,state,jtb);
            }
        });

        return view;
    }

    private void setInitalFrameRating() {
        if (fragmentManager.findFragmentByTag(RAT_FRAG_USER) != null) {
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(RAT_FRAG_USER)).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.main_rating_frame_container, ratingsFragmentUser, RAT_FRAG_USER).commit();
        }
        if (fragmentManager.findFragmentByTag(RAT_FRAG_TEACHER) != null) {
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(RAT_FRAG_TEACHER)).commit();
        }
    }

    public void stateChange(float process, State state, JellyToggleButton jtb) {
        if (state.equals(State.RIGHT_TO_LEFT)) {
            setInitalFrameRating();
        }
        if (state.equals(State.LEFT_TO_RIGHT)) {
            setSecondFrameRating();
        }
    }

    private void setSecondFrameRating() {
        if (fragmentManager.findFragmentByTag(RAT_FRAG_TEACHER) != null) {
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag(RAT_FRAG_TEACHER)).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.main_rating_frame_container, ratingsFragmentTeacher, RAT_FRAG_TEACHER).commit();
        }
        if (fragmentManager.findFragmentByTag(RAT_FRAG_USER) != null) {
            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag(RAT_FRAG_USER)).commit();
        }
    }
}
