package newsandtools.dingqiqi.com.application;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity栈管理
 * Created by dingqiiq on 2016/6/14.
 */
public class ActivityManager {

    private static Stack<Activity> mActivities;

    private ActivityManager() {
    }

    /**
     * 初始化Activity栈
     *
     * @return
     */
    public static Stack<Activity> getInstance() {
        if (mActivities == null) {
            mActivities = new Stack<>();
        }
        return mActivities;
    }

    /**
     * 关闭所有页面
     */
    public static void removeAllActivity() {
        for (int i = 0; i < mActivities.size(); i++) {
            mActivities.get(i).finish();
        }
        mActivities.clear();
    }

}
