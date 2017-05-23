package newsandtools.dingqiqi.com.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.view.LoadingWebView;

/**
 * @author: dingqiqi
 */
public class NewsWebViewActivity extends Activity implements View.OnClickListener {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private LoadingWebView mLoadingWebView;

    private String mTitle = "";
    private String mUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
        initData();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mLoadingWebView = (LoadingWebView) findViewById(R.id.wv_loading);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        mLoadingWebView.addProgressBar();
        mIvBack.setOnClickListener(this);
    }

    private void initData() {
        mTitle = getIntent().getStringExtra("title");
        mUrl = getIntent().getStringExtra("url");

        mLoadingWebView.loadMessageUrl(mUrl);
        mTvTitle.setText(mTitle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoadingWebView.destroyWebView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (mLoadingWebView.canGoBack())
                    mLoadingWebView.goBack();
                else {
                    finish();
                }
                break;
        }
    }

    /**
     * 按返回键时， 不退出程序而是返回WebView的上一页面
     */
    @Override
    public void onBackPressed() {
        if (mLoadingWebView.canGoBack())
            mLoadingWebView.goBack();
        else {
            super.onBackPressed();
        }
    }
}
