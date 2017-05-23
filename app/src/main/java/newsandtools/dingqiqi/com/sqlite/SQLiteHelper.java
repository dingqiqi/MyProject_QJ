package newsandtools.dingqiqi.com.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import newsandtools.dingqiqi.com.config.AppConfig;

/**
 * Created by dingqiqi on 2016/6/29.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static int version = 1;


    public SQLiteHelper(Context context, String name) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table if not exists " + AppConfig.TABLE_NAME + " (_id integer not null primary key autoincrement,time string not null," +
                "code string,text string not null,type integer not null)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
