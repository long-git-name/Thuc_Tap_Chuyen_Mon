package com.nguyenvanlong.quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.nguyenvanlong.quiz.database.DBHelper;
import com.nguyenvanlong.quiz.models.Question;

import java.util.ArrayList;
import java.util.Timer;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    DBHelper dbHelper;
    TextView txtContent, txtQuestion, txtCaseA, txtCaseB, txtCaseC, txtCaseD, txtTime;
    ArrayList<Question> listQuestions;
    int id1, trueCase, number, count = 0;
    CountDownTimer Timer;

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
        //question = listQuestions.get(count);
    }

    private void addEvents() {

        txtCaseA.setOnClickListener(this);
        txtCaseB.setOnClickListener(this);
        txtCaseC.setOnClickListener(this);
        txtCaseD.setOnClickListener(this);
    }
    //Hiển thị câu hỏi
    public void LoadQuestion(Question question) {

        txtCaseA.setBackgroundResource(R.drawable.bg_ans_corner);
        txtCaseB.setBackgroundResource(R.drawable.bg_ans_corner);
        txtCaseC.setBackgroundResource(R.drawable.bg_ans_corner);
        txtCaseD.setBackgroundResource(R.drawable.bg_ans_corner);

        number = count + 1;

        txtQuestion.setText("Câu " + number);
        txtContent.setText(question.getCauhoi());
        txtCaseA.setText("A. " + question.getCaseA());
        txtCaseB.setText("B. " + question.getCaseB());
        txtCaseC.setText("C. " + question.getCaseC());
        txtCaseD.setText("D. " + question.getCaseD());
        trueCase(question);

        // Bộ đếm thời gian cho mỗi câu hỏi
        Timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtTime.setText(millisUntilFinished/1000 + "s");
            }

            @Override
            public void onFinish() {
                txtTime.setText("Hết giờ!");

                if(count > 0){
                    showDialog("Bạn đã trả lời đúng" + count + " câu.");
                }
                else {
                    showDialog("Rất tiếc! Bạn đã hết thời gian.");
                }
            }
        }.start();
    }

    public void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
        builder.setTitle("Hết giờ!");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // gán đáp án đúng cho mỗi câu hỏi
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

                    // nếu chọn đúng đáp án
                    if (v.getId() == trueCase) {

                        Timer.cancel();     // Hủy bộ đếm khi sang câu mới
                        v.setBackgroundResource(R.drawable.bg_true_corner);

                        if(count < listQuestions.size() - 1){
                            count++;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    LoadQuestion(listQuestions.get(count));
                                }
                            },1000);
                        }
                    }

                    // nếu chọn sai đáp án
                    else {
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
                        //Hiển thị dialog thông báo
                        try{
                            Thread.sleep(1000);
                            final Dialog dialog = new Dialog(PlayActivity.this, R.style.cust_dialog);
                            dialog.setContentView(R.layout.finish_dialog);
                            dialog.setTitle("Thông báo!!!");
                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

                            TextView text = dialog.findViewById(R.id.txtDialog);
                            text.setText("Thật tiếc bạn đã trả lời sai!");

                            TextView diem = dialog.findViewById(R.id.txtDiem);
                            diem.setText("Bạn trả lời đúng " + count + " câu.");

                            dialog.setCancelable(false);
                            dialog.show();

                            Button btnOk = dialog.findViewById(R.id.btn_ok_finish);
                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 1000);
        }
    }

}
