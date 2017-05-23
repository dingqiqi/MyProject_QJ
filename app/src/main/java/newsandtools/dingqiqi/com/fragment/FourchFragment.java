package newsandtools.dingqiqi.com.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.adapter.WeatherAdapter;
import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.http.HttpRequest;
import newsandtools.dingqiqi.com.http.LocationService;
import newsandtools.dingqiqi.com.mode.WeatherForecastMode;
import newsandtools.dingqiqi.com.mode.WeatherMode;
import newsandtools.dingqiqi.com.util.DensityUtil;
import newsandtools.dingqiqi.com.util.URLUtil;
import newsandtools.dingqiqi.com.view.ProgressLoadView;

/**
 * Created by dingqiqi on 2016/6/15.
 */
public class FourchFragment extends Fragment implements BDLocationListener {
    /**
     * 是否第一次启动
     */
    private boolean mIsPrepared;
    /**
     * 是否显示
     */
    private boolean mIsVisibleHint;
    /**
     * 菜单
     */
    private ImageView mIvMenu;
    /**
     * 温度
     */
    private TextView mTvTemperature;
    /**
     * 地点
     */
    private TextView mTvAddress;
    /**
     * 天气
     */
    private TextView mTvWeather;
    /**
     * 风力相关
     */
    private LinearLayout mLLFeng;
    private TextView mTvFeng;
    private TextView mTvFengZhiShu;
    /**
     * 防晒相关
     */
    private LinearLayout mLLShai;
    private TextView mTvShai;
    private TextView mTvShaiZhiShu;
    /**
     * 穿衣相关
     */
    private LinearLayout mLLYi;
    private TextView mTvYi;
    private TextView mTvYiZhiShu;
    /**
     * 运动相关
     */
    private LinearLayout mLLDong;
    private TextView mTvDong;
    private TextView mTvDongZhiShu;

    private ListView mListView;
    /**
     * 定位
     */
    private LinearLayout mLLLocation;
    private TextView mTvSDInput;
    private TextView mTvZDInput;
    /**
     * 定位
     */
    private LocationClient mLocationClient;
    /**
     * 网络请求
     */
    private HttpURLConnection mConnection;
    /**
     * 加载布局
     */
    private View mLoadView;
    private ProgressLoadView mProgressLoadView;
    /**
     * 定位地址
     */
    private String mAddress;
    /**
     * 未来天气数据
     */
    private List<WeatherForecastMode> mList;
    /**
     * 未来天气适配器
     */
    private WeatherAdapter mAdapter;
    /**
     * 打开关闭动画
     */
    private ValueAnimator mAnimatorOpen;
    private ValueAnimator mAnimatorClose;
    /**
     * 是否打开
     */
    private boolean mIsOpen = true;
    /**
     * 下拉刷新
     */
    private SwipeRefreshLayout mRefreshLayout;
    /**
     * 天气数据
     */
    public WeatherMode mMode;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    mRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    });
                    break;
                case 0x02:
                    mLoadView.setVisibility(View.GONE);

                    String message = (String) msg.obj;
                    AppConfig.ShowMessage(getActivity(), message);
                    break;
                case 0x03:
                    mMode = (WeatherMode) msg.obj;

                    mTvTemperature.setText(mMode.getCurTemp());
                    mTvAddress.setText(mAddress);
                    mTvWeather.setText(mMode.getType());

                    if (mMode.getIndex().size() < 6) {
                        return;
                    }

                    mTvFeng.setText(mMode.getIndex().get(1).getName());
                    mTvFengZhiShu.setText(mMode.getIndex().get(1).getIndex());

                    mTvShai.setText(mMode.getIndex().get(2).getName());
                    mTvShaiZhiShu.setText(mMode.getIndex().get(2).getIndex());

                    mTvYi.setText(mMode.getIndex().get(3).getName());
                    mTvYiZhiShu.setText(mMode.getIndex().get(3).getIndex());

                    mTvDong.setText(mMode.getIndex().get(5).getName());
                    mTvDongZhiShu.setText(mMode.getIndex().get(5).getIndex());

                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsPrepared = true;
        setUserVisibleHint(mIsVisibleHint);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_fourth_layout, null, false);

        initView(mView);

        setListener();

        mIsPrepared = true;

        return mView;
    }

    private void initView(View mView) {
        mIvMenu = (ImageView) mView.findViewById(R.id.iv_menu);
        mTvTemperature = (TextView) mView.findViewById(R.id.tv_weather_temperature);
        mTvAddress = (TextView) mView.findViewById(R.id.tv_weather_address);
        mTvWeather = (TextView) mView.findViewById(R.id.tv_weather_weather);

        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.refreshLayout);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mLLFeng = (LinearLayout) mView.findViewById(R.id.ll_weather_1);
        mTvFeng = (TextView) mView.findViewById(R.id.tv_feng);
        mTvFengZhiShu = (TextView) mView.findViewById(R.id.tv_feng_zhishu);

        mLLShai = (LinearLayout) mView.findViewById(R.id.ll_weather_2);
        mTvShai = (TextView) mView.findViewById(R.id.tv_shai);
        mTvShaiZhiShu = (TextView) mView.findViewById(R.id.tv_shai_zhishu);

        mLLYi = (LinearLayout) mView.findViewById(R.id.ll_weather_3);
        mTvYi = (TextView) mView.findViewById(R.id.tv_yi);
        mTvYiZhiShu = (TextView) mView.findViewById(R.id.tv_yi_zhishu);

        mLLDong = (LinearLayout) mView.findViewById(R.id.ll_weather_4);
        mTvDong = (TextView) mView.findViewById(R.id.tv_dong);
        mTvDongZhiShu = (TextView) mView.findViewById(R.id.tv_dong_zhishu);

        mLLLocation = (LinearLayout) mView.findViewById(R.id.linear_location);
        mTvSDInput = (TextView) mView.findViewById(R.id.tv_sd_input);
        mTvZDInput = (TextView) mView.findViewById(R.id.tv_zd_input);

        mListView = (ListView) mView.findViewById(R.id.lv_weather);

        mLoadView = mView.findViewById(R.id.loadLayout);
        mLoadView.setVisibility(View.GONE);
        mProgressLoadView = (ProgressLoadView) mLoadView.findViewById(R.id.progressLoadView);

        mLocationClient = LocationService.getInstance(getActivity());
        mLocationClient.registerLocationListener(this);

        mList = new ArrayList<>();
        mAdapter = new WeatherAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
    }

    private void setListener() {
        mTvZDInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.IsQuickClick()) {
                    mLoadView.setVisibility(View.VISIBLE);
                    mProgressLoadView.setText("定位中...");
                    LocationService.startLocation(getActivity());
                }
            }
        });

        mAnimatorOpen = ValueAnimator.ofInt(DensityUtil.dip2px(getActivity(), 40));
        mAnimatorOpen.setDuration(500);
        mAnimatorOpen.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLLLocation.getLayoutParams();
                params.height = value;
                mLLLocation.setLayoutParams(params);
            }
        });

        mAnimatorClose = ValueAnimator.ofInt(DensityUtil.dip2px(getActivity(), 40));
        mAnimatorClose.setDuration(500);
        mAnimatorClose.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLLLocation.getLayoutParams();
                params.height = DensityUtil.dip2px(getActivity(), 40) - value;
                mLLLocation.setLayoutParams(params);
            }
        });

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if ("".equals(mAddress)) {
                    mRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    requestData(mAddress);
                }
            }
        });

        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsOpen) {
                    mAnimatorClose.start();
                } else {
                    mAnimatorOpen.start();
                }
                mIsOpen = !mIsOpen;
            }
        });

        mLLFeng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode != null) {
                    AppConfig.ShowMessage(getActivity(), mMode.getIndex().get(1).getDetails());
                }
            }
        });

        mLLShai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode != null) {
                    AppConfig.ShowMessage(getActivity(), mMode.getIndex().get(2).getDetails());
                }
            }
        });

        mLLYi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode != null) {
                    AppConfig.ShowMessage(getActivity(), mMode.getIndex().get(3).getDetails());
                }
            }
        });

        mLLDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode != null) {
                    AppConfig.ShowMessage(getActivity(), mMode.getIndex().get(5).getDetails());
                }
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleHint = isVisibleToUser;

        if (getUserVisibleHint() && mIsPrepared) {

            if ("".equals(AppConfig.getLocationAddress(getActivity()))) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLLLocation.getLayoutParams();
                params.height = DensityUtil.dip2px(getActivity(), 40);
                mLLLocation.setLayoutParams(params);
                mIsOpen = true;
            } else {
                mLoadView.setVisibility(View.VISIBLE);
                mIsOpen = false;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLLLocation.getLayoutParams();
                params.height = 0;
                mLLLocation.setLayoutParams(params);
                mAddress = AppConfig.getLocationAddress(getActivity());
                requestData(mAddress);
            }

        } else {

        }

    }

    /**
     * 请求数据
     */
    public void requestData(String address) {
        Map<String, String> params = new HashMap<>();

        params.put("cityname", address);

        mProgressLoadView.setText("加载天气中...");
        mConnection = HttpRequest.HttpGet(URLUtil.WEATHER_URL, params, new HttpRequest.ResultCallBack() {
            @Override
            public void onSuccess(String result) {
                mHandler.sendEmptyMessage(0x01);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int errNum = jsonObject.getInt("errNum");
                    //返回成功
                    if (errNum == 0) {
                        jsonObject = jsonObject.getJSONObject("retData");
                        Gson gson = new Gson();
                        //今天天气相关
                        WeatherMode mode = gson.fromJson(jsonObject.getString("today"), WeatherMode.class);
                        //未来天气相关
                        WeatherForecastMode mode1 = gson.fromJson(jsonObject.toString(), WeatherForecastMode.class);
                        mList.clear();
                        mList.addAll(mode1.getForecast());

                        Message message = mHandler.obtainMessage();
                        message.what = 0x03;
                        message.obj = mode;
                        mHandler.sendMessage(message);
                    } else {
                        Message message = mHandler.obtainMessage();
                        message.what = 0x02;
                        message.obj = jsonObject.getString("errMsg");
                        mHandler.sendMessage(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Message message = mHandler.obtainMessage();
                    message.what = 0x02;
                    message.obj = "解析数据失败";
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void onFail(String result) {
                mHandler.sendEmptyMessage(0x01);

                Message message = mHandler.obtainMessage();
                message.what = 0x02;
                message.obj = result;
                mHandler.sendMessage(message);
            }
        }, 1);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationClient.unRegisterLocationListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        String city = location.getCity();// 城市
        String district = location.getDistrict();// 区（或县）
        String street = location.getStreet();// 街道
        String streetNumber = location.getStreetNumber();// 街道号
        double latitude = location.getLatitude();// 纬度
        double longitude = location.getLongitude();// 精度

        AppConfig.logI(city + district + street);
        mLocationClient.stop();

        if (district == null || "".equals(district)) {
            Message message = mHandler.obtainMessage();
            message.what = 0x02;
            message.obj = "位置获取失败";
            mHandler.sendMessage(message);
            return;
        }

        //去掉区获取市
        if ("浦东新区".equals(district)) {
            mAddress = district.substring(0, district.length() - 2);
        } else {
            mAddress = district.substring(0, district.length() - 1);
        }

        AppConfig.setLocationAddress(getActivity(), mAddress);
        requestData(mAddress);

        mAnimatorClose.start();
        mIsOpen = false;
    }

}
