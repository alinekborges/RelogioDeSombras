package com.alieeen.bulbdialclock.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.alieeen.bulbdialclock.MainActivity;
import com.alieeen.bulbdialclock.R;

/**
 * Created by alinekborges on 07/07/15.
 */
public class MainFragment extends Fragment {



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitiesFragment.
     */

    private TimePicker timePicker;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.main_fragment, container, false);

        timePicker = (TimePicker) v.findViewById(R.id.timePicker1);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                Log.i("Bulbdial", "on time change + " + i + " " + i1);
                MainActivity context = (MainActivity) getActivity();
                //context.oldSend(i, i1);
                context.setHour(i);
                context.setMinute(i1);

            }
        });

        return v;
    }
}