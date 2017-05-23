package newsandtools.dingqiqi.com.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhy.autolayout.AutoLayoutActivity;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.application.ActivityManager;
import newsandtools.dingqiqi.com.config.SystemBar;

/**
 * Created by dingqiqi on 2016/6/14.
 */
//public class BaseActivity extends AutoLayoutActivity {
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().add(this);

        SystemBar.setSystemBarColor(this, R.color.colorPrimaryStatusBar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
    }
}
