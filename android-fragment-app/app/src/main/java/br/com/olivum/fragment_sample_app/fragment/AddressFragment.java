package br.com.olivum.fragment_sample_app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.olivum.fragment_sample_app.R;

public class AddressFragment extends Fragment {
    private static final String TAG = "AddressFragment";

    private View layoutInflater = null;

    public AddressFragment() {
    }

    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();

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

        layoutInflater = inflater.inflate(R.layout.fragment_address, container, false);

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