package newsandtools.dingqiqi.com.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.config.AppConfig;

/**
 * dingqiqi
 */
public class DrawLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;

    private View mDragView;
    private View mMenuView;

    private float mFlag = 0.5f;
    private int mWidth;
    private int mMenuWidth;

    private int mDownX = -1;
    private int mUpX = -1;

    private boolean mIsOpen = false;
    /**
     * 是否可以滑动，解决Viewpager滑动冲突
     */
    private boolean mIsCanScroll = true;
    /**
     * 状态监听
     */
    private PageChangeListener mPageChangeListener;

    public DrawLayout(Context context) {
        super(context, null);
    }

    @SuppressWarnings("deprecation")
    public DrawLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDragHelper = ViewDragHelper.create(this, 1.0f, mCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWidth = manager.getDefaultDisplay().getWidth();

        mMenuWidth = (int) (mWidth * mFlag);
        //背景
        setBackgroundResource(R.mipmap.main_background);
    }

    /**
     * 设置页面监听
     *
     * @param mPageChangeListener
     */
    public void setmPageChangeListener(PageChangeListener mPageChangeListener) {
        this.mPageChangeListener = mPageChangeListener;
    }

    /**
     * 打开或者关闭页面
     */
    public void OpenOrClose() {
        Log.i("aaa", "OpenOrClose");

        if (mIsOpen) {
            mDragHelper.smoothSlideViewTo(mDragView, 0, 0);
            invalidate();
        } else {
            mDragHelper.smoothSlideViewTo(mDragView, mMenuWidth, 0);
            invalidate();
        }

        mIsOpen = !mIsOpen;

        if (mPageChangeListener != null) {
            mPageChangeListener.OpenOrClose(mIsOpen);
        }
    }

    public void setmIsCanScroll(boolean mIsCanScroll) {
        this.mIsCanScroll = mIsCanScroll;
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View arg0, int arg1) {
//            Log.i("aaa", "tryCaptureView" + mIsCanScroll);
            if (mIsCanScroll) {
                return arg0 == mDragView;
            } else {
                return false;
            }
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            Log.i("aaa", "clampViewPositionHorizontal" + mIsCanScroll);
            int leftBond = getPaddingLeft();
            left = Math.max(leftBond, left);

            left = Math.min(left, mMenuWidth);

            return left;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
//            Log.i("aaa", "onEdgeDragStarted" + mIsCanScroll);

            if (mIsCanScroll) {
                mDragHelper.captureChildView(mDragView, pointerId);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.i("aaa", mDragView.getX() + "--->mUpX" + mUpX);

            if (mUpX != -1) {
                // 左划
                if (mDownX - mUpX >= 0) {
                    if (mIsOpen) {
                        AppConfig.logI("DrawLayout");
                        if (mDownX - mUpX > mMenuWidth / 4) {

                            mDragHelper.smoothSlideViewTo(mDragView, 0, 0);
                            invalidate();

                            mIsOpen = false;

                            AppConfig.logI("DrawLayout关闭" + mIsOpen);

                            if (mPageChangeListener != null) {
                                mPageChangeListener.OpenOrClose(false);
                            }
                        } else {
                            AppConfig.logI("111");
                            if (mPageChangeListener != null) {
                                mPageChangeListener.OpenOrClose(true);
                            }
                            mDragHelper.smoothSlideViewTo(mDragView,
                                    mMenuWidth, 0);
                            invalidate();
                        }
                    }
                    // 右滑
                } else if (mDownX - mUpX < 0) {
                    if (!mIsOpen) {
                        AppConfig.logI("DrawLayout");
                        if (Math.abs(mDownX - mUpX) > mMenuWidth / 4) {

                            mDragHelper.smoothSlideViewTo(mDragView,
                                    mMenuWidth, 0);
                            invalidate();

                            mIsOpen = true;

                            AppConfig.logI("DrawLayout打开" + mIsOpen);

                            if (mPageChangeListener != null) {
                                mPageChangeListener.OpenOrClose(true);
                            }
                        } else {
                            AppConfig.logI("111");
                            if (mPageChangeListener != null) {
                                mPageChangeListener.OpenOrClose(false);
                            }
                            mDragHelper.smoothSlideViewTo(mDragView, 0, 0);
                            invalidate();
                        }
                    }
                }

                mDownX = -1;
                mUpX = -1;
            } else {
                if (mPageChangeListener != null) {
                    mPageChangeListener.OpenOrClose(false);
                }
                mDragHelper.smoothSlideViewTo(mDragView, 0, 0);
                invalidate();
            }

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
//            Log.i("aaa", "onViewPositionChanged");

            if (changedView == mDragView) {

                float value = left * 1.0f / mMenuWidth;

                setAlphaAndSize(value);
            }

        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

    };

    private void setAlphaAndSize(float value) {
        // 主面板：缩放到0.9
        float inverse = 1 - value * 0.1f;
        mDragView.setScaleY(inverse);

        // (仿qq，菜单滑动一小部分)
        mMenuView.setTranslationX((value - 1) * mMenuWidth / 4);
        mMenuView.setTranslationY((value - 0.5f) * mMenuWidth / 4);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() < 2) {
            throw new IllegalArgumentException("最少包含两个View");
        }

        mMenuView = getChildAt(0);
        mDragView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.i("aaa", "onLayout");

        if (mMenuView != null && mDragView != null) {
            mMenuView.layout(0, 0, mMenuWidth, b - t);
            mDragView.layout(0, 0, mWidth, b - t);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        AppConfig.logI("Drawlayout onTouchEvent");

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                mUpX = (int) event.getX();
                break;

            default:
                break;
        }

        mDragHelper.processTouchEvent(event);

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsOpen) {
            return true;
        }

        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public interface PageChangeListener {
        /**
         * 打开或者关闭
         *
         * @param isOpen true 打开 false 关闭
         */
        public void OpenOrClose(boolean isOpen);
    }

}
