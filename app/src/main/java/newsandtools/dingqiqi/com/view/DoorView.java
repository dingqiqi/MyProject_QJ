package newsandtools.dingqiqi.com.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import newsandtools.dingqiqi.com.R;

public class DoorView extends FrameLayout {

    private Scroller mScroller;

    private int mHeight;
    private int mWidth;

    private int mDownY;

    private ImageView mImageView;

    private boolean mIsClose = false;

    private StatusCallBack mCallBack;

    public DoorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager mManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(metrics);

        mHeight = metrics.heightPixels;
        mWidth = metrics.widthPixels;

        Interpolator interpolator = new BounceInterpolator();
        mScroller = new Scroller(context, interpolator);

        mImageView = new ImageView(context);
        LayoutParams mParams = new LayoutParams(mWidth, mHeight);

        mImageView.setLayoutParams(mParams);
        mImageView.setBackgroundResource(R.mipmap.door_bg);

        addView(mImageView);
    }

    public void setmCallBack(StatusCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public DoorView(Context context) {
        super(context, null);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mImageView.layout(0, 0, mWidth, b - t);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                int mCurrent = (int) event.getY();
                int mDel = mCurrent - mDownY;
                // 向上滑
                if (mDel < 0) {
                    scrollTo(0, -mDel);
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                mCurrent = (int) event.getY();
                mDel = mCurrent - mDownY;

                if (mDel < 0) {
                    if (Math.abs(mCurrent - mDownY) > mHeight / 4) {
                        mIsClose = true;
                        mScroller.startScroll(0, getScrollY(), 0, mHeight, 500);
                        invalidate();

                        if (mCallBack != null) {
                            mCallBack.Open();
                        }
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 800);
                        invalidate();
                    }
                }
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else {
            if (mIsClose) {
                mImageView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 打开页面回调
     */
    public interface StatusCallBack {
        public void Open();
    }
}
