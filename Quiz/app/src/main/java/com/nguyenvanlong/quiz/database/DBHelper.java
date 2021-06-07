package com.nguyenvanlong.quiz.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.nguyenvanlong.quiz.R;
import com.nguyenvanlong.quiz.models.Question;
import com.nguyenvanlong.quiz.LevelActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "question.sqlite";
    private static String DB_PATH ="";
    private final Context myContext;
    public static final String TABLE_NAME="Question";
    LevelActivity levelActivity;

    public static SQLiteDatabase database = null;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        if(Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }else {
            DB_PATH = "/data/data/" + context.getOpPackageName() + "/databases/";
        }
        this.myContext = context;
    }

    public void CreateDB() {
        boolean dbExist = CheckDB();
        if (!dbExist) {
            this.getReadableDatabase();
            this.close();
            try {
                CopyDBFromAsset();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean CheckDB() {
        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.exists();
    }

    private void CopyDBFromAsset() throws IOException {
            InputStream inputStream = myContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer))> 0){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
    }

    public ArrayList<Question> getData(){
        database = myContext.openOrCreateDatabase(DB_NAME,Context.MODE_PRIVATE,null);
        ArrayList<Question> questionArrayList = new ArrayList<>();
            for (int i = 1; i < 6;) {

                String table = TABLE_NAME + i + "";
                String sql = "SELECT * FROM " + table + " ORDER BY random() ";
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery(sql, null);
                questionArrayList.clear();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String question = cursor.getString(1);
                    String caseA = cursor.getString(2);
                    String caseB = cursor.getString(3);
                    String caseC = cursor.getString(4);
                    String caseD = cursor.getString(5);
                    int trueCase = cursor.getInt(6);
                    Question question1 = new Question(id, question, caseA, caseB, caseC, caseD, trueCase);
                    questionArrayList.add(question1);
                }
                cursor.close();
                break;
            }
        return questionArrayList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
