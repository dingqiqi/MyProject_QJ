package newsandtools.dingqiqi.com.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.adapter.JokeAdapter;
import newsandtools.dingqiqi.com.adapter.LoopPageAdapter;
import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.util.URLUtil;
import newsandtools.dingqiqi.com.http.HttpRequest;
import newsandtools.dingqiqi.com.mode.JokeMode;
import newsandtools.dingqiqi.com.util.DensityUtil;
import newsandtools.dingqiqi.com.view.LoopViewPager;
import newsandtools.dingqiqi.com.view.MarqueeView;

/**
 * Created by dingqiqi on 2016/6/15.
 */
public class SecondFragment extends Fragment {
    /**
     * 是否第一次启动
     */
    private boolean mIsPrepared;
    /**
     * 是否显示
     */
    private boolean mIsVisibleHint;

    /**
     * 跑马灯布局
     */
    private MarqueeView mMarqueeView;
    /**
     * 热点布局
     */
    private LinearLayout mLinearRD;
    /**
     * 点击动画
     */
    private ValueAnimator mValueAnimator;
    private ValueAnimator mValueAnimator1;
    /**
     * 热点是否展开
     */
    private boolean mIsOpen = false;
    /**
     * 笑话列表
     */
    private ListView mListView;
    /**
     * 笑话数据源
     */
    private List<JokeMode> mList;
    /**
     * 下拉刷新
     */
    private SwipeRefreshLayout mRefreshLayout;
    /**
     * 笑话适配器
     */
    private JokeAdapter mAdapter;
    /**
     * 当前页数
     */
    private int mPage = 1;
    /**
     * 是刷新还是加载
     */
    private boolean mIsRefresh = true;
    /**
     * 所有页数(加载更多使用)
     */
    private int mAllCount = 0;
    /**
     * 加载更多
     */
    private LinearLayout mLinearFoot;
    /**
     * 加载的view
     */
    private View mLoadView;
    /**
     * 广告页相关
     */
    private List<View> mListPager;
    private LoopViewPager mLoopViewPager;
    private LoopPageAdapter mLoopPageAdapter;
    /**
     * 圆点相关
     * 圆点间距
     */
    private int mPointWidth = 0;
    /**
     * 圆点父布局
     */
    private LinearLayout mPointLayout;
    /**
     * 页面数量
     */
    private int mPageNum = 5;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    AppConfig.ShowMessage(getActivity(), msg.obj.toString());
                    break;
                case 0x02:
                    JokeMode jokeMode = (JokeMode) msg.obj;
                    mList.addAll(jokeMode.getContentlist());
                    mAdapter.notifyDataSetChanged();
                    break;
                case 0x03:
                    mLoadView.setVisibility(View.GONE);
                    mLinearFoot.setVisibility(View.GONE);
                    break;
                default:
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

        View mView = inflater.inflate(R.layout.fragment_second_layout, null, false);

        initView(mView);
        setListener();

        mIsPrepared = true;

        return mView;
    }

    /**
     * 初始化布局
     *
     * @param mView
     */
    private void initView(View mView) {
        mLoadView = mView.findViewById(R.id.loadLayout);
        mLinearRD = (LinearLayout) mView.findViewById(R.id.ll_rd);
        mMarqueeView = (MarqueeView) mView.findViewById(R.id.tv_rd);
        mListView = (ListView) mView.findViewById(R.id.listview);
        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeRefreshLayout);

        View footView = LayoutInflater.from(getActivity()).inflate(R.layout.foot_view_layout, null, false);

        mLinearFoot = (LinearLayout) footView.findViewById(R.id.linear_foot_view);

        mLoadView.setVisibility(View.GONE);
        mLinearFoot.setVisibility(View.GONE);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mListView.addFooterView(footView);

        mList = new ArrayList<>();
        mAdapter = new JokeAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);

        //广告页初始化
        mListPager = new ArrayList<>();
        for (int i = 0; i < mPageNum; i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.loop_viewpager_layout,
                    null, false);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText("第" + i + "页");
            mListPager.add(view);
        }

        mLoopViewPager = (LoopViewPager) mView.findViewById(R.id.loopviewpager);
        mLoopPageAdapter = new LoopPageAdapter(getActivity(), mListPager);
        mLoopViewPager.setAdapter(mLoopPageAdapter);
        mLoopViewPager.setCurrentItem(1);
        mLoopViewPager.setOnPageChangeListener(mChangeListener);

        //圆点初始化
        mPointLayout = (LinearLayout) mView.findViewById(R.id.ll_point_hint);

        // 加载出来
        mPointLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mPointWidth = mPointLayout.getChildAt(1).getLeft()
                                - mPointLayout.getChildAt(0).getLeft();
                    }
                });

        for (int i = 0; i < mPageNum; i++) {
            ImageView imageView = new ImageView(getActivity());

            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (i != 0) {
                imageView.setBackgroundResource(R.drawable.shape_oval_gray);
                params.leftMargin = 20;
            } else {
                imageView.setBackgroundResource(R.drawable.shape_oval_red);
            }

            imageView.setLayoutParams(params);
            mPointLayout.addView(imageView);
        }
    }

    /**
     * 初始化圆点颜色
     */
    private void initReadPoint() {
        for (int i = 0; i < mPointLayout.getChildCount(); i++) {
            mPointLayout.getChildAt(i).setBackgroundResource(R.drawable.shape_oval_gray);
        }
    }

    /**
     * 广告页滑动监听
     */
    private ViewPager.OnPageChangeListener mChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            initReadPoint();

            mPointLayout.getChildAt(arg0).setBackgroundResource(R.drawable.shape_oval_red);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * 设置监听
     */
    private void setListener() {
        mValueAnimator = ValueAnimator.ofInt(DensityUtil.dip2px(getActivity(), 120));
        mValueAnimator1 = ValueAnimator.ofInt(DensityUtil.dip2px(getActivity(), 120));

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLinearRD.getLayoutParams();
                layoutParams.height = value;
                mLinearRD.setLayoutParams(layoutParams);
            }
        });
        mValueAnimator.setDuration(300);

        mValueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLinearRD.getLayoutParams();
                layoutParams.height = DensityUtil.dip2px(getActivity(), 120) - value;
                mLinearRD.setLayoutParams(layoutParams);
            }
        });
        mValueAnimator1.setDuration(300);

        mMarqueeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsOpen) {
                    mValueAnimator1.start();
                } else {
                    mValueAnimator.start();
                }
                mIsOpen = !mIsOpen;
            }
        });

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRefresh();
                RequestData();
            }
        });

        //加载更多
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //滑动到最后
                        if (view.getLastVisiblePosition() == view.getAdapter().getCount() - 1) {
                            if (mAllCount > mPage) {
                                if (mLinearFoot.getVisibility() == View.GONE) {
                                    mLinearFoot.setVisibility(View.VISIBLE);
                                }
                            } else {
                                mLinearFoot.setVisibility(View.GONE);
                            }
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        //加载更多
        mLinearFoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadView.setVisibility(View.VISIBLE);
                initLoad();
                RequestData();
            }
        });

    }

    /**
     * 初始化刷新数据
     */
    private void initRefresh() {
        mIsRefresh = true;
        mPage = 1;
    }

    /**
     * 初始化加载数据
     */
    private void initLoad() {
        mIsRefresh = false;
        mPage += 1;
    }

    /**
     * 初始化加载失败数据
     */
    private void errorLoad() {
        if (mPage > 1) {
            mPage -= 1;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleHint = isVisibleToUser;

        if (getUserVisibleHint() && mIsPrepared) {
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            });
            initRefresh();
            RequestData();
        } else {

        }

    }

    private void RequestData() {
        Map<String, String> params = new HashMap<>();

        //页数
        params.put("page", mPage + "");

        HttpURLConnection mConnection = HttpRequest.HttpGet(URLUtil.JOKE_URL, params, new HttpRequest.ResultCallBack() {
            @Override
            public void onSuccess(String result) {
                mHandler.sendEmptyMessage(0x03);
                if (mIsRefresh) {
                    mList.clear();
                    mRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    });
                }

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("showapi_res_code");

                    if ("0".equals(code)) {
                        jsonObject = jsonObject.getJSONObject("showapi_res_body");
                        mAllCount = jsonObject.getInt("allPages");

                        Gson gson = new Gson();
                        JokeMode jokeMode = gson.fromJson(jsonObject.toString(), JokeMode.class);

                        Message message = mHandler.obtainMessage();
                        message.what = 0x02;
                        message.obj = jokeMode;
                        mHandler.sendMessage(message);
                    } else {
                        errorLoad();
                        Message message = mHandler.obtainMessage();
                        message.what = 0x01;
                        message.obj = result;
                        mHandler.sendMessage(message);
                    }

                } catch (JSONException e) {
                    errorLoad();
                    Message message = mHandler.obtainMessage();
                    message.what = 0x01;
                    message.obj = "数据解析错误";
                    mHandler.sendMessage(message);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
                if (mIsRefresh) {
                    mRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    });
                }
                errorLoad();
                AppConfig.logE("request onFail");

                Message message = mHandler.obtainMessage();
                message.what = 0x01;
                message.obj = result;
                mHandler.sendMessage(message);
            }
        }, 1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
