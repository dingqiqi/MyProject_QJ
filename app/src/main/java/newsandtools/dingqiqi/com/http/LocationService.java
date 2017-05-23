package newsandtools.dingqiqi.com.http;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import newsandtools.dingqiqi.com.config.AppConfig;

/**
 * Created by Administrator on 2016/7/1.
 */
public class LocationService {

    private static LocationClient mLocationClient;

    /**
     * 初始化定位
     *
     * @param context
     * @return
     */
    public static LocationClient getInstance(Context context) {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(context);
            LocationClientOption option = new LocationClientOption();
            option.setCoorType("bd09ll");// bd09ll:设置坐标类型，为百度经纬坐标系
            option.setIsNeedAddress(true);
            option.setTimeOut(30000);
            option.setOpenGps(true);// 打开GPS定位
            option.setScanSpan(0);// 设置定时定位的时间间隔为1s
            mLocationClient.setLocOption(option);// 设置定位方式
        }

        return mLocationClient;
    }

    /**
     * 启动定位
     *
     * @param context
     */
    public static void startLocation(Context context) {
        if (mLocationClient == null) {
            AppConfig.logE("初始化定位");
            return;
        }
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }

        mLocationClient.start();
    }

}
