package newsandtools.dingqiqi.com.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.util.DensityUtil;

/**
 * app杂项
 * Created by Administrator on 2016/6/14.
 */
public class AppConfig {
    /**
     * 表名
     */
    public static String TABLE_NAME = "chat_table";
    /**
     * 数据库名称
     */
    public static String SQLITE_NAME = "qiju";
    /**
     * sharedpreference的名称，用于存储天气地址
     */
    public static String SP_NAME = "qiju_weather";
    /**
     * 是否打印日志
     */
    private static boolean IS_LOG = true;
    /**
     * 是否存储错误日志
     */
    public static boolean LOG_ERR_SAVE = true;

    /**
     * 错误日志名称
     */
    public static String LOG_ERR_FILE_NAME = "err_log.txt";
    /**
     * 日志TAG
     */
    private static String TAG = "DQQ_NEWS_TOOLS";
    /**
     * 退出时间
     */
    private static long mExitTime = 0;
    /**
     * 新闻_笑话_Appkey
     */
//    public static String APP_KEY = "9e1eb77cdb36db3831681eb257eb0d4d";
    public static String APP_KEY = "daecad6f9a5974996ed07e622923d40b";
    /**
     * 机器人appkey
     */
//    public static String JQR_APP_KEY = "611ab782322abbdeb9449670218821c3";
    public static String JQR_APP_KEY = "611ab782322abbdeb9449670218821c3";
    /**
     * 百度定位key
     */
    public static String BAIDU_LOCATION_KEY = "mDlOT4o941vjsK7aqBz2x9LnvascK4Eb";
    /**
     * 快速点击记录时间
     */
    public static long mQucikTime = 0;
    /**
     * 震动
     */
    private static Vibrator mVibrator;
    /**
     * 提示音
     */
    private static Ringtone mRingtone;

    /**
     * 打印日志
     *
     * @param log
     */
    public static void logI(String log) {
        if (IS_LOG) {
            Log.i(TAG, log);
        }
    }

    /**
     * 打印错误日志
     *
     * @param log
     */
    public static void logE(String log) {
        if (IS_LOG) {
            Log.e(TAG, log);
        }
    }

    /**
     * 双击推出提示
     *
     * @param context
     */
    public static void showExit(Context context) {
        if (System.currentTimeMillis() - mExitTime > 1000) {
            mExitTime = System.currentTimeMillis();
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();
        } else {
            ((Activity) context).finish();
        }
    }

    /**
     * 显示消息
     *
     * @param context
     * @param message
     */
    public static void ShowMessage(Context context, String message) {
        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view_layout, null, false);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        TextView textView = (TextView) view.findViewById(R.id.tv_toast);
        textView.setText(message);
        toast.show();
    }

    /**
     * 快速点击判断
     *
     * @return true 不是快速点击
     */
    public static boolean IsQuickClick() {
        long time = System.currentTimeMillis();

        if (time - mQucikTime > 500) {
            return true;
        } else {
            mQucikTime = System.currentTimeMillis();
        }

        return false;
    }

    /**
     * 震动
     *
     * @param context
     * @param time
     */
    public static void Vibrator(Context context, long time) {
        if (mVibrator == null) {
            mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }

        mVibrator.vibrate(time);
    }

    /**
     * 播放提示音
     *
     * @param context
     */
    public static void RingTone(Context context) {
        if (mRingtone == null) {
            Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mRingtone = RingtoneManager.getRingtone(context, notificationUri);
        }

        if (mRingtone.isPlaying()) {
            mRingtone.stop();
        }

        mRingtone.play();
    }

    /**
     * 获取定位位置
     *
     * @param context
     * @return
     */
    public static String getLocationAddress(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        return preferences.getString("location", "");
    }

    /**
     * 获取定位位置
     *
     * @param context
     * @return
     */
    public static void setLocationAddress(Context context, String address) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        preferences.edit().putString("location", address).commit();
    }

    /**
     * 是否进入过应用
     *
     * @param context
     * @return
     */
    public static boolean getFirstInto(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SQLITE_NAME, Context.MODE_PRIVATE);

        return preferences.getBoolean("first", false);
    }

    /**
     * 是否进入过应用
     *
     * @param context
     * @return
     */
    public static void saveFirstInto(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SQLITE_NAME, Context.MODE_PRIVATE);

        preferences.edit().putBoolean("first", true).commit();
    }
}
