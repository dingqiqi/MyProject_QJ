package newsandtools.dingqiqi.com.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.util.DateUtil;
import newsandtools.dingqiqi.com.util.FileUtil;

/**
 * Created by dingqiqi on 2016/6/14.
 */
public class MyApplication extends Application {

    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        //百度地图初始化
        SDKInitializer.initialize(this);

        AutoLayoutConifg.getInstance().useDeviceSize().init(this);

        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        mUncaughtExceptionHandler = new CustomUncaughtException();
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
    }

    private class CustomUncaughtException implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            AppConfig.logI(thread.getName() + "-->" + ex.getMessage());

            try {
                saveForceCloseInfo2File(ex);
            } catch (Exception e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        ActivityManager.removeAllActivity();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void saveForceCloseInfo2File(Throwable ex) throws Exception {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString() + "\n";
        AppConfig.logI(result);

        if (AppConfig.LOG_ERR_SAVE) {
            String path = FileUtil.getSDCardPath();
            if (path == null) {
                path = getCacheDir().getAbsolutePath();
            }
            File file = new File(path + "/" + DateUtil.getCurrentTime() + AppConfig.LOG_ERR_FILE_NAME);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(result.getBytes());
            fos.close();
        }
    }

}
