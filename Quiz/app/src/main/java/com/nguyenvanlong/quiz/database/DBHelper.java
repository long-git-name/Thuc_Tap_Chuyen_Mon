package com.nguyenvanlong.quiz.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.nguyenvanlong.quiz.MainActivity;
import com.nguyenvanlong.quiz.R;
import com.nguyenvanlong.quiz.models.Question;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.nguyenvanlong.quiz.MainActivity.getTableName;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "question.sqlite";
    private static String DB_PATH ="";
    private final Context myContext;
    public static final String TABLE_NAME="Question";
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

            String sql = "SELECT * FROM " + getTableName() + " ORDER BY random() ";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            questionArrayList.clear();
            while (cursor.moveToNext()) {
                questionArrayList.add(new Question(cursor.getInt(0),cursor.getString(1),
                        cursor.getString(2),cursor.getString(3),cursor.getString(4),
                        cursor.getString(5), cursor.getInt(6)));
            }
            cursor.close();

        return questionArrayList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
