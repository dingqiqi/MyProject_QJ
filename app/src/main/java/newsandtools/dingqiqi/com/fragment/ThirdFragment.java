package newsandtools.dingqiqi.com.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.adapter.ChatAdapter;
import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.util.URLUtil;
import newsandtools.dingqiqi.com.http.HttpRequest;
import newsandtools.dingqiqi.com.mode.ChatMode;
import newsandtools.dingqiqi.com.sqlite.DBHelper;
import newsandtools.dingqiqi.com.util.DateUtil;

/**
 * Created by dingqiqi on 2016/6/15.
 */
public class ThirdFragment extends Fragment {
    /**
     * 是否第一次启动
     */
    private boolean mIsPrepared;
    /**
     * 是否显示
     */
    private boolean mIsVisibleHint;
    /**
     * 对话列表
     */
    private RecyclerView mRecyclerView;
    /**
     * 数据源
     */
    private List<ChatMode> mList;
    /**
     * 适配器
     */
    private ChatAdapter mAdapter;
    /**
     * 布局管理类
     */
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * 输入框
     */
    private EditText mEditText;
    /**
     * 发送按钮
     */
    private Button mBtnSend;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    ChatMode mode = (ChatMode) msg.obj;
                    mList.add(mode);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(mList.size() - 1);
                    break;
                case 0x02:
                    String str = (String) msg.obj;
                    AppConfig.ShowMessage(getActivity(), str);
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

        View mView = inflater.inflate(R.layout.fragment_thrid_layout, null, false);

        initView(mView);
        setListener();

        mIsPrepared = true;

        return mView;
    }

    private void initView(View mView) {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycleview);
        mEditText = (EditText) mView.findViewById(R.id.edittext);
        mBtnSend = (Button) mView.findViewById(R.id.btn_send);

        mList = new ArrayList<>();
        mAdapter = new ChatAdapter(getActivity(), mList);

        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setListener() {
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEditText.getText().toString();

                if (!AppConfig.IsQuickClick()) {
                    return;
                }

                if (msg.isEmpty()) {
                    AppConfig.ShowMessage(getActivity(), "对话不能为空");
                } else {
                    ChatMode mode = new ChatMode();
                    mode.setText(msg);
                    mode.setType(1);
                    mode.setTime(DateUtil.getCurrentTime());
                    mList.add(mode);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(mList.size() - 1);
                    //清空输入
                    mEditText.getText().clear();

                    DBHelper.insertData(getActivity(), mode);

                    requestData(msg);
                }

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleHint = isVisibleToUser;

        if (getUserVisibleHint() && mIsPrepared) {
            mList.clear();
            mList.addAll(DBHelper.queryAllData(getActivity()));

            ChatMode mode = new ChatMode();
            mode.setText("你好，机器人MM为您服务！！！");
            mode.setType(0);
            mode.setTime(DateUtil.getCurrentTime());
            if (mList.size() > 0) {
                mode.setHint(true);
            }
            mList.add(mode);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(mList.size() - 1);
        } else {

        }

    }

    /**
     * 与机器人对话
     *
     * @param msg
     */
    public void requestData(String msg) {
        Map<String, String> params = new HashMap<>();

        params.put("key", AppConfig.JQR_APP_KEY);
        params.put("info", msg);
        params.put("dtype", "json");
//        params.put("loc", "");
//        params.put("lon", "");
//        params.put("lat", "");
//        params.put("userid", "");

        HttpRequest.HttpGet(URLUtil.JQR_URL, params, new HttpRequest.ResultCallBack() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    int error_code = jsonObject.getInt("error_code");

                    if (error_code == 0) {
                        AppConfig.Vibrator(getActivity(), 600);
                        AppConfig.RingTone(getActivity());

                        jsonObject = jsonObject.getJSONObject("result");
                        String text = jsonObject.getString("text");
                        String code = jsonObject.getString("code");

                        ChatMode mode = new ChatMode();
                        mode.setText(text);
                        mode.setCode(code);
                        mode.setTime(DateUtil.getCurrentTime());
                        mode.setType(0);

                        DBHelper.insertData(getActivity(), mode);

                        Message message = mHandler.obtainMessage();
                        message.what = 0x01;
                        message.obj = mode;
                        mHandler.sendMessage(message);
                    } else {
                        Message message = mHandler.obtainMessage();
                        message.what = 0x02;
                        message.obj = jsonObject.getString("reason");
                        mHandler.sendMessage(message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String result) {
                Message message = mHandler.obtainMessage();
                message.what = 0x02;
                message.obj = result;
                mHandler.sendMessage(message);
            }
        }, 0);
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
