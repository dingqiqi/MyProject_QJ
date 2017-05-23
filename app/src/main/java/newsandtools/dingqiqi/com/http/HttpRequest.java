package newsandtools.dingqiqi.com.http;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import newsandtools.dingqiqi.com.config.AppConfig;

/**
 * Created by dingqiqi on 2016/6/15.
 */
public class HttpRequest {
    /**
     * 设置请求参数
     *
     * @param param
     * @param mConnection
     */
    private static void setParam(Map<String, String> param, HttpURLConnection mConnection) {

        for (Map.Entry<String, String> map : param.entrySet()) {
            if (map.getKey() != null && map.getValue() != null) {
                mConnection.setRequestProperty(map.getKey(), map.getValue());
                AppConfig.logI("参数：" + map.getKey() + "=" + map.getValue());
            }
        }

    }

    /**
     * 获取请求参数
     *
     * @param param
     */
    private static String getParam(Map<String, String> param) throws UnsupportedEncodingException {
        String url = "";

        for (Map.Entry<String, String> map : param.entrySet()) {
            if (map.getKey() != null && map.getValue() != null) {
                url = url + map.getKey() + "=" + URLEncoder.encode(map.getValue(), "utf-8") + "&";
                AppConfig.logI("参数：" + map.getKey() + "=" + URLEncoder.encode(map.getValue(), "utf-8"));
            }
        }
        //取出最后一个&
        return url.substring(0, url.length() - 1);
    }

    /**
     * get 请求
     *
     * @param url       请求地址
     * @param param     请求参数
     * @param mCallBack 请求回调
     * @param key       1 传key  0 不传key
     */
    public static HttpURLConnection HttpGet(String url, Map<String, String> param, final ResultCallBack mCallBack, int key) {
        try {
            URL mUrl = new URL(url + "?" + getParam(param));
            final HttpURLConnection mConnection = (HttpURLConnection) mUrl.openConnection();
            //请求方式
            mConnection.setRequestMethod("GET");
            mConnection.setConnectTimeout(10 * 1000);
            mConnection.setRequestProperty("Content-type", "text/html");
            mConnection.setRequestProperty("contentType", "utf-8");
            mConnection.setRequestProperty("Accept-Charset", "utf-8");
            if (key == 1) {
                mConnection.setRequestProperty("apikey", AppConfig.APP_KEY);
            }
            //允许输出
            mConnection.setDoOutput(true);
//            setParam(param, mConnection);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mConnection.connect();
                        InputStream mInputStream = mConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream));

                        StringBuffer mStringBuffer = new StringBuffer();
                        String str = new String();

                        while ((str = reader.readLine()) != null) {
                            mStringBuffer.append(str);
                        }

                        String result = "";
                        if (mStringBuffer != null) {
                            result = new String(mStringBuffer.toString().getBytes("utf-8"));
                            new String();
                        }

                        //返回成功
                        if (mConnection.getResponseCode() == 200) {
                            mCallBack.onSuccess(result);
                        } else {
                            mCallBack.onFail(result);
                        }

                        reader.close();
                        mInputStream.close();
                        AppConfig.logI("返回结果" + result);
                    } catch (IOException e) {
                        mCallBack.onFail("网络异常");
                        e.printStackTrace();
                    }

                }
            }).start();

            return mConnection;
        } catch (Exception e) {
            mCallBack.onFail("网络异常");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * post 请求
     *
     * @param url       请求网址
     * @param param     请求参数
     * @param mCallBack 回调
     */
    public static HttpURLConnection HttpPost(String url, Map<String, String> param, final ResultCallBack mCallBack) {
        try {
            URL mUrl = new URL(url);
            final HttpURLConnection mConnection = (HttpURLConnection) mUrl.openConnection();
            //请求方式
            mConnection.setRequestMethod("POST");
            mConnection.setConnectTimeout(10 * 1000);
            mConnection.setRequestProperty("Content-type", "text/html");
            mConnection.setRequestProperty("contentType", "utf-8");
            mConnection.setRequestProperty("Accept-Charset", "utf-8");
            //允许输出
            mConnection.setDoOutput(true);

            setParam(param, mConnection);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mConnection.connect();
                        InputStream mInputStream = mConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream));

                        StringBuffer mStringBuffer = new StringBuffer();
                        String str = new String();

                        while ((str = reader.readLine()) != null) {
                            mStringBuffer.append(str);
                        }

                        String result = "";
                        if (mStringBuffer != null) {
                            result = new String(mStringBuffer.toString().getBytes("utf-8"));
                            new String();
                        }

                        //返回成功
                        if (mConnection.getResponseCode() == 200) {
                            mCallBack.onSuccess(result);
                        } else {
                            mCallBack.onFail(result);
                        }

                        reader.close();
                        mInputStream.close();
                        AppConfig.logI("返回结果" + result);
                    } catch (IOException e) {
                        mCallBack.onFail("网络异常");
                        e.printStackTrace();
                    }

                }
            }).start();

            return mConnection;
        } catch (Exception e) {
            mCallBack.onFail("网络异常");
            e.printStackTrace();
        }

        return null;
    }


    public interface ResultCallBack {
        /**
         * 成功回调
         *
         * @param result
         */
        public void onSuccess(String result);

        /**
         * 失败回调
         *
         * @param result
         */
        public void onFail(String result);
    }

}
