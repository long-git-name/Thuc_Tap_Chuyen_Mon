package com.nguyenvanlong.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

public class ChangeActivity extends AppCompatActivity {
    RelativeLayout layoutChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        layoutChange = findViewById(R.id.layout_change);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);

        layoutChange.setAnimation(animation);
        CountDownTimer t = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(ChangeActivity.this, PlayActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();
            }
        }.start();
    }
}