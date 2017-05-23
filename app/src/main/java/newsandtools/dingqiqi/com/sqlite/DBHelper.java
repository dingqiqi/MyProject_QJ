package newsandtools.dingqiqi.com.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.mode.ChatMode;

/**
 * Created by dingqiqi on 2016/6/29.
 */
public class DBHelper {

    private static SQLiteDatabase mDatabase;

    /**
     * 获取SQLiteDatabase对象
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase getInstance(Context context) {

        if (mDatabase == null) {
            SQLiteHelper mHelper = new SQLiteHelper(context, AppConfig.SQLITE_NAME);

            try {
                mDatabase = mHelper.getWritableDatabase();
            } catch (Exception e) {
                mDatabase = mHelper.getReadableDatabase();
            }
        }

        return mDatabase;
    }

    /**
     * 插入聊天记录
     *
     * @param context
     * @param chatMode
     */
    public static int insertData(Context context, ChatMode chatMode) {
        ContentValues values = new ContentValues();

        values.put("time", chatMode.getTime());
        values.put("code", chatMode.getCode());
        values.put("text", chatMode.getText());
        values.put("type", chatMode.getType());

        long cloum = getInstance(context).insert(AppConfig.TABLE_NAME, null, values);

        return (int) cloum;
    }

    /**
     * 查询所有聊天数据
     *
     * @return
     */
    public static List<ChatMode> queryAllData(Context context) {
        List<ChatMode> list = new ArrayList<>();

        Cursor cursor = getInstance(context).query(AppConfig.TABLE_NAME, null, null, null, null, null, "time asc");

        while (cursor.moveToNext()) {
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String code = cursor.getString(cursor.getColumnIndex("code"));
            String text = cursor.getString(cursor.getColumnIndex("text"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));

            ChatMode mode = new ChatMode();
            mode.setText(text);
            mode.setTime(time);
            mode.setType(type);
            mode.setCode(code);

            list.add(mode);
        }

        return list;
    }

}
