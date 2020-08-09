package br.com.olivum.fragment_sample_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import br.com.olivum.fragment_sample_app.R;
import br.com.olivum.fragment_sample_app.fragment.AddressFragment;
import br.com.olivum.fragment_sample_app.fragment.LocationFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button buttonLocation;
    private Button buttonAddress;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize controls

        buttonLocation = findViewById(R.id.button_location);

        buttonAddress = findViewById(R.id.button_address);

        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureFragment("location");
            }
        });

        buttonAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureFragment("address");
            }
        });
    }

    private void configureFragment(String type) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (type) {
            case "location": {
                if (fragment != null && fragment instanceof LocationFragment) {
                    Log.d(TAG, "Location fragment already added");

                    return;
                }

                buttonLocation.setEnabled(false);

                Log.d(TAG, "Adding location fragment");

                if (fragment != null) {
                    Log.d(TAG, "Removing current fragment");

                    fragmentTransaction.remove(fragment);

                    fragment = null;
                }

                fragment = new LocationFragment();

                buttonAddress.setEnabled(true);

                break;
            }
            case "address": {
                if (fragment != null && fragment instanceof AddressFragment) {
                    Log.d(TAG, "Address fragment already added");

                    return;
                }

                buttonAddress.setEnabled(false);

                Log.d(TAG, "Adding address fragment");

                if (fragment != null) {
                    Log.d(TAG, "Removing current fragment");

                    fragmentTransaction.remove(fragment);

                    fragment = null;
                }

                fragment = new AddressFragment();

                buttonLocation.setEnabled(true);

                break;
            }
            default:
                return;
        }

        fragmentTransaction.add(R.id.linear_layout_fragment, fragment);

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}