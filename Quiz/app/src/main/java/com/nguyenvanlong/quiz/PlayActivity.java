package com.nguyenvanlong.quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyenvanlong.quiz.database.DBHelper;
import com.nguyenvanlong.quiz.models.Question;

import java.util.ArrayList;
import java.util.Random;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    DBHelper dbHelper;
    TextView txtContent, txtQuestion, txtCaseA, txtCaseB, txtCaseC, txtCaseD, txtTime;
    ArrayList<Question> listQuestions;
    int id1, trueCase, number, count = 0, i;
    CountDownTimer Timer;
    MediaPlayer mediaPlayer;
    Random random;
    int [] sound_correct = {
            R.raw.correct_answer,
            R.raw.correct_answer_2,
            R.raw.correct_answer_3
    };

    //Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        dbHelper = new DBHelper(this);
        dbHelper.CreateDB();
        linkViews();
        LoadQuestion(listQuestions.get(count));
        addEvents();
    }

    private void linkViews() {
        txtContent = findViewById(R.id.txtContent);
        txtQuestion = findViewById(R.id.txtQuestion);
        txtCaseA = findViewById(R.id.txtCaseA);
        txtCaseB = findViewById(R.id.txtCaseB);
        txtCaseC = findViewById(R.id.txtCaseC);
        txtCaseD = findViewById(R.id.txtCaseD);
        txtTime = findViewById(R.id.txtTime);

        listQuestions = dbHelper.getData();
        random = new Random();
        i = random.nextInt(3);
    }

    private void addEvents() {

        txtCaseA.setOnClickListener(this);
        txtCaseB.setOnClickListener(this);
        txtCaseC.setOnClickListener(this);
        txtCaseD.setOnClickListener(this);
    }
    //Hi???n th??? c??u h???i
    public void LoadQuestion(Question question) {

        txtCaseA.setBackgroundResource(R.drawable.bg_ans_corner);
        txtCaseB.setBackgroundResource(R.drawable.bg_ans_corner);
        txtCaseC.setBackgroundResource(R.drawable.bg_ans_corner);
        txtCaseD.setBackgroundResource(R.drawable.bg_ans_corner);

        number = count + 1;

        txtQuestion.setText("C??u " + number);
        txtContent.setText(question.getCauhoi());
        txtCaseA.setText("A. " + question.getCaseA());
        txtCaseB.setText("B. " + question.getCaseB());
        txtCaseC.setText("C. " + question.getCaseC());
        txtCaseD.setText("D. " + question.getCaseD());
        trueCase(question);
        timer();
    }

    public void timer(){
        // B??? ?????m th???i gian cho m???i c??u h???i
        Timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtTime.setText(millisUntilFinished/1000 + "s");
            }

            @Override
            public void onFinish() {
                txtTime.setText("H???t gi???!");
                if(count > 0) {
                    try {
                        gameOver(0, "R???t ti???c! B???n ???? h???t th???i gian.", "B???n tr??? l???i ????ng " + count + " c??u.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        gameOver(0, "R???t ti???c! B???n ???? h???t th???i gian.", "");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    // g??n ????p ??n ????ng cho m???i c??u h???i
    private void trueCase(Question question) {

        id1 = question.getTrueCase();
        switch (id1) {
            case 1:
                trueCase = R.id.txtCaseA;
                break;
            case 2:
                trueCase = R.id.txtCaseB;
                break;
            case 3:
                trueCase = R.id.txtCaseC;
                break;
            case 4:
                trueCase = R.id.txtCaseD;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtCaseA ||
                v.getId() == R.id.txtCaseB ||
                v.getId() == R.id.txtCaseC ||
                v.getId() == R.id.txtCaseD) {
            v.setBackgroundResource(R.drawable.bg_select_corner);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    // n???u ch???n ????ng ????p ??n
                    if (v.getId() == trueCase) {

                        Timer.cancel();     // H???y b??? ?????m khi sang c??u m???i
                        v.setBackgroundResource(R.drawable.bg_true_corner);

                        if(count < listQuestions.size() - 1){
                            count++;
                            playSound(sound_correct[i]);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        nextQuestion("Ch??c m???ng", "B???n ???? tr??? l???i ????ng.");
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },1000);
                        }else {
                            try {
                                playSound(R.raw.victory);
                                gameOver(1000,"Ch??c m???ng b???n ","B???n ???? ho??n th??nh "+ listQuestions.size() + " c??u.");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // n???u ch???n sai ????p ??n
                    else {
                        playSound(R.raw.boo);
                        Timer.cancel();
                        switch (trueCase) {
                            case R.id.txtCaseA:
                                v.setBackgroundResource(R.drawable.bg_false_corner);
                                txtCaseA.setBackgroundResource(R.drawable.bg_true_corner);
                                break;
                            case R.id.txtCaseB:
                                v.setBackgroundResource(R.drawable.bg_false_corner);
                                txtCaseB.setBackgroundResource(R.drawable.bg_true_corner);
                                break;
                            case R.id.txtCaseC:
                                v.setBackgroundResource(R.drawable.bg_false_corner);
                                txtCaseC.setBackgroundResource(R.drawable.bg_true_corner);
                                break;
                            case R.id.txtCaseD:
                                v.setBackgroundResource(R.drawable.bg_false_corner);
                                txtCaseD.setBackgroundResource(R.drawable.bg_true_corner);
                                break;
                        }
                            //Hi???n th??? dialog th??ng b??o
                        try {
                            gameOver(1000,"Th???t ti???c b???n ???? tr??? l???i sai!","B???n tr??? l???i ????ng " + count + " c??u.");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }, 1000);
        }
    }

    public void nextQuestion(String message1, String message2) throws InterruptedException {
        i = random.nextInt(3);

        final Dialog dialog = new Dialog(PlayActivity.this, R.style.cust_dialog);
        dialog.setContentView(R.layout.finish_dialog);

        dialog.setTitle("Th??ng b??o!!!");
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

        TextView text = dialog.findViewById(R.id.txtDialog);
        text.setText(message1);

        TextView diem = dialog.findViewById(R.id.txtDiem);
        diem.setText(message2);

        dialog.setCancelable(false);
        Button btnOk = dialog.findViewById(R.id.btn_ok_finish);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LoadQuestion(listQuestions.get(count));
            }
        });
        dialog.show();
    }

        //Dialog th??ng b??o
    public void gameOver(Integer time,String message1, String message2) throws InterruptedException {

        Thread.sleep(time);

        final Dialog dialog = new Dialog(PlayActivity.this, R.style.cust_dialog);
        dialog.setContentView(R.layout.finish_dialog);

        dialog.setTitle("Th??ng b??o!!!");
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

        TextView text = dialog.findViewById(R.id.txtDialog);
        text.setText(message1);

        TextView diem = dialog.findViewById(R.id.txtDiem);
        diem.setText(message2);

        dialog.setCancelable(false);
        Button btnOk = dialog.findViewById(R.id.btn_ok_finish);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
    public void playSound(int type) {
        mediaPlayer = MediaPlayer.create(this, type);
        mediaPlayer.start();
    }
}
