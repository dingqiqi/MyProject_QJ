package newsandtools.dingqiqi.com.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.mode.ChannelMode;

/**
 * Created by dingqiqi on 2016/6/17.
 */
public class HttpUtil {
    /**
     * 频道列表
     */
    private static List<ChannelMode> mChannelList = new ArrayList<>();

    /**
     * 获取频道列表
     *
     * @return
     */
    public static List<ChannelMode> getChannelList(Context context) {
        if (mChannelList.size() == 0) {
            String[] str = context.getResources().getStringArray(R.array.channel_list);

            for (int i = 0; i < str.length; i++) {
                String name = str[i].split(",")[1];
                String value = str[i].split(",")[0];

                ChannelMode mode = new ChannelMode(name, value);
                mChannelList.add(mode);
            }

        }

        return mChannelList;
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnect(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.isAvailable();
        }

        return false;
    }


}
