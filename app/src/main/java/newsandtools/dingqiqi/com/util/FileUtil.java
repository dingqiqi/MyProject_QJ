package newsandtools.dingqiqi.com.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.http.HttpRequest;
import newsandtools.dingqiqi.com.http.ThreadPoolImg;

/**
 * Created by Administrator on 2016/6/14.
 */
public class FileUtil {

    /**
     * 文件夹名称
     */
    public static String FILE_NAME = "DQQ_FILE";
    /**
     * 缓存文件夹名称
     */
    public static String CACHE_FILE_NAME = "DQQ_FILE/cache";
    /**
     * 文件夹名称
     */
    public static String SP_FILE_NAME = "DQQ_FILE";
    /**
     * 是否下载图片
     */
    public static boolean mDownImageEnable = true;

    /**
     * 写入配置,是否下载图片
     *
     * @param flag
     */
    public static void saveDownImageEnable(Context context, boolean flag) {
        SharedPreferences mPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);

        mPreferences.edit().putBoolean("DOWN_IMG", flag).commit();
        mDownImageEnable = flag;
    }

    /**
     * 读取配置，是否下载图片
     */
    public static void readDownImageEnable(Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        mDownImageEnable = mPreferences.getBoolean("DOWN_IMG", true);
    }


    /**
     * 获取SDCard路径
     *
     * @return
     */
    public static String getSDCardPath() {
        //SDCard存在
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + CACHE_FILE_NAME;

            File file = new File(path);

            if (!file.exists()) {
                file.mkdirs();
            }

            return path;
        }
        return null;
    }

    /**
     * 返回缓存里的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static File getCacheDirPath(Context context, String fileName) {
        String path = context.getCacheDir() + "/" + fileName;

        File file = new File(path);

        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 文件是否存在
     *
     * @return
     */
    private static boolean fileIsExists(String filePath) {
        if (filePath == null || "".equals(filePath)) {
            return false;
        }
        File file = new File(filePath);

        return file.exists();
    }

    /**
     * 文件是否存在
     *
     * @return
     */
    private static Bitmap getFileBitmap(String filePath) {
        if (filePath == null || "".equals(filePath)) {
            return null;
        }
        File file = new File(filePath);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * url中截取name
     *
     * @param url
     * @return
     */
    private static String urlSubName(String url) {
        int index = url.lastIndexOf("/");
        if (index != -1) {
            String name = url.substring(index + 1);

            return name;
        }
        return null;
    }

    /**
     * 显示url图片
     *
     * @param mImageView
     * @param url
     * @param mCallBack
     * @return
     */
    public static Bitmap showImage(Context context, final ImageView mImageView, String url, final DownImgCallBack mCallBack) {
        String name = urlSubName(url);

        if (name != null) {
            String path = getSDCardPath() + "/" + name;
            //文件存在
            if (fileIsExists(path)) {
                return getFileBitmap(path);
            } else {
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Bitmap bitmap = (Bitmap) msg.obj;
                        mCallBack.downImgSuccess(mImageView, bitmap);
                    }
                };

                if (!mDownImageEnable) {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                    Message message = handler.obtainMessage();
                    message.obj = bitmap;
                    handler.sendMessage(message);
                } else {
                    ThreadPoolImg.HttpDownImg(url, new HttpRequest.ResultCallBack() {
                        @Override
                        public void onSuccess(String result) {
                            Bitmap bitmap = getFileBitmap(result);
                            if (bitmap != null) {
                                //为了通讯
                                Message message = handler.obtainMessage();
                                message.obj = bitmap;
                                handler.sendMessage(message);
                            }
                        }

                        @Override
                        public void onFail(String result) {

                        }
                    });
                }

            }
        }

        return null;
    }


    public interface DownImgCallBack {
        public void downImgSuccess(ImageView imageView, Bitmap bitmap);
    }

}
