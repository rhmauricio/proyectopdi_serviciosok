package com.rhmauricio.proyectopdi.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rhmauricio.proyectopdi.Fragments.EmotionFragment;
import com.rhmauricio.proyectopdi.Fragments.StaticsFragment;
import com.rhmauricio.proyectopdi.R;

public class PpalActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppal);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            EmotionFragment firstFragment = new EmotionFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_emotion:
                    // Create a new Fragment to be placed in the activity layout
                    EmotionFragment firstFragment = new EmotionFragment();

                    // In case this activity was started with special instructions from an
                    // Intent, pass the Intent's extras to the fragment as arguments
                    //firstFragment.setArguments(getIntent().getExtras());

                    // Add the fragment to the 'fragment_container' FrameLayout
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, firstFragment).commit();
                    return true;
                case R.id.navigation_statics:
                    // Create a new Fragment to be placed in the activity layout
                    StaticsFragment secondFragment = new StaticsFragment();

                    // In case this activity was started with special instructions from an
                    // Intent, pass the Intent's extras to the fragment as arguments
                    //firstFragment.setArguments(getIntent().getExtras());

                    // Add the fragment to the 'fragment_container' FrameLayout
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, secondFragment).commit();
                    return true;

            }
            return false;
        }
    };
}
