package com.nguyenvanlong.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ramotion.foldingcell.FoldingCell;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnExit;
    FoldingCell foldingCell;
    public static TextView txtLv1,txtLv2,txtLv3,txtLv4,txtLv5;
    public static int id;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkViews();
        addEvents();
        playSoundLoop(R.raw.backgroundaudio);
    }

    private void linkViews() {
        btnExit = findViewById(R.id.btnExit);
        foldingCell = findViewById(R.id.folding_cell);
        txtLv1 = findViewById(R.id.txtLv1);
        txtLv2 = findViewById(R.id.txtLv2);
        txtLv3 = findViewById(R.id.txtLv3);
        txtLv4 = findViewById(R.id.txtLv4);
        txtLv5 = findViewById(R.id.txtLv5);

    }
    private void addEvents() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bạn có muốn thoát trò chơi?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foldingCell.toggle(false);
            }
        });

        txtLv1.setOnClickListener(this);
        txtLv2.setOnClickListener(this);
        txtLv3.setOnClickListener(this);
        txtLv4.setOnClickListener(this);
        txtLv5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        id = v.getId();

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Bạn đã sẵn sàng bắt đầu trò chơi?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, ChangeActivity.class));
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void playSoundLoop(int type) {
        mediaPlayer = MediaPlayer.create(this, type);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(0.3f,0.3f);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
