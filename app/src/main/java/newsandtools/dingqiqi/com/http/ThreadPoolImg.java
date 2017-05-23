package newsandtools.dingqiqi.com.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.util.FileUtil;

/**
 * 图片下载线程池
 * Created by Administrator on 2016/6/15.
 */
public class ThreadPoolImg {
    /**
     * 线程数量
     */
    private static int THREAD_NUM = 3;

    private static ExecutorService mService;

    /**
     * 获取线程池实例
     *
     * @return
     */
    public static ExecutorService getInstance() {
        if (mService == null) {
            mService = Executors.newFixedThreadPool(THREAD_NUM);
        }
        return mService;
    }

    /**
     * get 请求
     *
     * @param url       请求地址
     * @param mCallBack 请求回调
     */
    public static void HttpDownImg(final String url, final HttpRequest.ResultCallBack mCallBack) {
        try {
            URL mUrl = new URL(url);
            final HttpURLConnection mConnection = (HttpURLConnection) mUrl.openConnection();
            //请求方式
            //mConnection.setRequestMethod("GET");
            //mConnection.setConnectTimeout(10 * 1000);
           // mConnection.setRequestProperty("apikey", AppConfig.APP_KEY);
            //允许输出
            //mConnection.setDoOutput(true);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mConnection.connect();
                        InputStream mInputStream = mConnection.getInputStream();
                        //返回成功
                        if (mConnection.getResponseCode() == 200) {
                            String path = FileUtil.getSDCardPath();
                            int index = url.lastIndexOf("/");

                            if (index != -1 && path != null) {
                                String name = url.substring(index + 1);
                                File file = new File(path + "/" + name);

                                FileOutputStream outputStream = new FileOutputStream(file);

                                int length;
                                while ((length = mInputStream.read()) != -1) {
                                    outputStream.write(length);
                                }
                                outputStream.flush();
                                outputStream.close();
                                mCallBack.onSuccess(path + "/" + name);
                            }
                        }

                        mInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            getInstance().execute(thread);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
