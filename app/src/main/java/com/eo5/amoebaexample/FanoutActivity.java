package com.eo5.amoebaexample;

import android.graphics.CornerPathEffect;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.eo5.amoeba.views.FanOutButton;

public class FanoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabAngled = (FloatingActionButton)findViewById(R.id.fanout_button_angled);
        fabAngled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FanoutActivity.this.finish();
            }
        });

        FloatingActionButton topButton = (FloatingActionButton)findViewById(R.id.fanout_button_top);
        final FanOutButton fanOutButton = (FanOutButton)findViewById(R.id.fanout_button);
        final ViewGroup rootLayout = fanOutButton.getAnchorLayout();
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(rootLayout, "Snackbar test", Snackbar.LENGTH_LONG).show();
            }
        });



    }

}
