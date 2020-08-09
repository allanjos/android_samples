package br.com.olivum.fragment_sample_app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.olivum.fragment_sample_app.R;

public class LocationFragment extends Fragment {
    private static final String TAG = "LocationFragment";

    private View layoutInflater = null;

    public LocationFragment() {
    }

    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");

        layoutInflater = inflater.inflate(R.layout.fragment_location_form, container, false);

        return layoutInflater;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");

        super.onDestroyView();

        if (layoutInflater != null) {
            layoutInflater = null;
        }

        System.gc();
    }
}