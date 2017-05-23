package newsandtools.dingqiqi.com.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import newsandtools.dingqiqi.com.config.AppConfig;

public class CustomFragmentViewpager extends ViewPager {
    /**
     * 是否滑动
     */
    private boolean mIsScroll = true;

    public CustomFragmentViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFragmentViewpager(Context context) {
        super(context, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        AppConfig.logI("CustomFragmentViewpager mIsScroll=" + mIsScroll);

        //drawlayout打开的时候，viewpager不处理滑动
        if (!mIsScroll) {
            return false;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        try {
            super.onLayout(changed, l, t, r, b);
        } catch (Exception e) {

        }
    }

//    @Override
//    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
//
//        return super.canScroll(v, checkV, dx, x, y);
//    }

    public void setIsScroll(boolean mIsScroll) {
        AppConfig.logE("-->CustomFragmentViewpager setIsScrol" + mIsScroll);
        this.mIsScroll = mIsScroll;
    }

}
