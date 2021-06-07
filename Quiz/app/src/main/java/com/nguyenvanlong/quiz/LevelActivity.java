package com.nguyenvanlong.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LevelActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView txtLv1,txtLv2,txtLv3,txtLv4,txtLv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        linkViews();
        addEvents();
    }

    private void linkViews() {
        txtLv1 = findViewById(R.id.txtLv1);
        txtLv2 = findViewById(R.id.txtLv2);
        txtLv3 = findViewById(R.id.txtLv3);
        txtLv4 = findViewById(R.id.txtLv4);
        txtLv5 = findViewById(R.id.txtLv5);
    }

    private void addEvents() {
        txtLv1.setOnClickListener(this);
        txtLv2.setOnClickListener(this);
        txtLv3.setOnClickListener(this);
        txtLv4.setOnClickListener(this);
        txtLv5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(LevelActivity.this, PlayActivity.class));
    }
}
