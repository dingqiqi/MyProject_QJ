package newsandtools.dingqiqi.com.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.activity.NewsWebViewActivity;
import newsandtools.dingqiqi.com.adapter.NewsBaseAdapter;
import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.util.URLUtil;
import newsandtools.dingqiqi.com.http.HttpRequest;
import newsandtools.dingqiqi.com.mode.NewsMode;

/**
 * Created by dingqiqi on 2016/6/15.
 */
public class NewsFragment extends Fragment {
    /**
     * 是否第一次启动
     */
    private boolean mIsPrepared;
    /**
     * 是否显示
     */
    private boolean mIsVisibleHint;
    /**
     * 网络请求
     */
    private HttpURLConnection mConnection;
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * 请求的value
     */
    private String mValue;
    /**
     * 新闻列表
     */
    private ListView mListView;
    /**
     * 新闻数据源
     */
    private List<NewsMode> mList;
    /**
     * 适配器
     */
    private NewsBaseAdapter mAdapter;
    /**
     * 加载的view
     */
    private View mLoadView;
    /**
     * 下拉刷新
     */
    private SwipeRefreshLayout mRefreshLayout;
    /**
     * 是否是刷新
     */
    private boolean mIsRefresh = true;
    /**
     * footView最外层布局
     */
    private LinearLayout mLinearLayout;
    /**
     * 加载更多
     */
    private LinearLayout mLinearFoot;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    AppConfig.ShowMessage(getActivity(), msg.obj.toString());
                    break;
                case 0x02:
                    NewsMode newsMode = (NewsMode) msg.obj;
                    mList.addAll(newsMode.getArticle());
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mValue = getArguments().getString("value");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_news_layout, null, false);

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
        mRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipeRefreshLayout);
        mListView = (ListView) mView.findViewById(R.id.listview);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        View footView = LayoutInflater.from(getActivity()).inflate(R.layout.foot_view_layout, null, false);

        mLinearLayout = (LinearLayout) footView.findViewById(R.id.linear);
        mLinearFoot = (LinearLayout) footView.findViewById(R.id.linear_foot_view);

        mLoadView.setVisibility(View.GONE);
        mLinearFoot.setVisibility(View.GONE);

        mList = new ArrayList<>();
        mAdapter = new NewsBaseAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
        mListView.addFooterView(footView);
    }

    /**
     * 设置监听
     */
    private void setListener() {
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
                            if (mLinearFoot.getVisibility() == View.GONE) {
                                mLinearFoot.setVisibility(View.VISIBLE);
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
        //查看新闻
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsWebViewActivity.class);
                intent.putExtra("title", getResources().getString(R.string.app_name));
                intent.putExtra("url", mList.get(position).getUrl());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsPrepared = true;
        setUserVisibleHint(mIsVisibleHint);
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
            CancelData();
        }

    }

    /**
     * 取消请求
     */
    private void CancelData() {
//        AppConfig.logE("news cancel");
//        if (mConnection != null) {
//            mConnection.disconnect();
//        }
        if (mList != null) {
            mList.clear();
            mAdapter.notifyDataSetChanged();
        }
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

    /**
     * 请求数据
     */
    private void RequestData() {
        // AppConfig.logE(" news request");
        Map<String, String> params = new HashMap<>();

        params.put("id", mValue);
        params.put("page", mPage + "");

        mConnection = HttpRequest.HttpGet(URLUtil.NRES_URL, params, new HttpRequest.ResultCallBack() {
            @Override
            public void onSuccess(String result) {
                AppConfig.logI("onSuccess");
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
                    String code = jsonObject.getString("code");
                    //成功
                    if ("0".equals(code)) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        NewsMode newsMode = gson.fromJson(data, NewsMode.class);

                        Message message = mHandler.obtainMessage();
                        message.what = 0x02;
                        message.obj = newsMode;
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
                errorLoad();
                AppConfig.logI("onFail");
                mHandler.sendEmptyMessage(0x03);
                if (mIsRefresh) {
                    mRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    });
                }

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
        CancelData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CancelData();
    }
}
