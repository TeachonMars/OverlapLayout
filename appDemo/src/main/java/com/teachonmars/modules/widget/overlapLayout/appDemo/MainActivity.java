package com.teachonmars.modules.widget.overlapLayout.appDemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.teachonmars.modules.widget.overlapLayout.OverlapLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView gravitySelector = findViewById(R.id.gravitySelector);
        final GravitySelectorAdapter adapter = new GravitySelectorAdapter(this);
        final OverlapLayout overlap = findViewById(R.id.overlap);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                overlap.setGravity(adapter.getGravity());
            }
        });
        gravitySelector.setAdapter(adapter);

        initOverlapContent(R.id.overlap);
        initOverlapContent(R.id.overlapHorStart);
        initOverlapContent(R.id.overlapHorCenter);
        initOverlapContent(R.id.overlapHorEnd);
        initOverlapContent(R.id.overlapVerTop);
        initOverlapContent(R.id.overlapVerCenter);
        initOverlapContent(R.id.overlapVerBottom);
        initOverlapContent(R.id.overlap7);
    }


    void initOverlapContent(int viewResId) {
        OverlapLayout layout = findViewById(viewResId);
        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_AppCompat_Display1);
            textView.setBackgroundColor(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            textView.setText(" " + i + " ");
            layout.addView(textView);
            ((OverlapLayout.LayoutParams) textView.getLayoutParams()).setMargins(random.nextInt(20), random.nextInt(20), random.nextInt(20), random.nextInt(20));
        }
    }
}
