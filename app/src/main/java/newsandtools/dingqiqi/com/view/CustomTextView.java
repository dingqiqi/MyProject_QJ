package newsandtools.dingqiqi.com.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import newsandtools.dingqiqi.com.config.AppConfig;

/**
 * Created by Administrator on 2016/6/22.
 */
public class CustomTextView extends LinearLayout {

    private Context mContext;
    /**
     * 初始高度
     */
    private int mInitHeight = 0;
    /**
     * 最大高度
     */
    private int mMaxHeight = 0;

    private int mCount = 0;
    /**
     * 是否关闭
     */
    private boolean mIsClose = true;
    /**
     * 打开关闭动画
     */
    private ValueAnimator mAnimeClose;
    private ValueAnimator mAnimeOpen;

    private TextView mTv;
    /**
     * 最多显示行数
     */
    private int mInitNum = 3;

    public CustomTextView(Context context) {
        super(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mMaxHeight > mInitHeight) {
                if (mIsClose) {
                    if (mAnimeOpen == null) {
                        mAnimeOpen = ValueAnimator.ofInt(mMaxHeight - mInitHeight);
                        mAnimeOpen.setDuration(300);
                        mAnimeOpen.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int height = (int) animation.getAnimatedValue();
                                AppConfig.logI(mInitHeight + height + " --》");

                                LayoutParams layoutParams = (LayoutParams) CustomTextView.this.getLayoutParams();
                                layoutParams.height = mInitHeight + height;
                                CustomTextView.this.setLayoutParams(layoutParams);
                            }
                        });
                    }

                    if (mAnimeClose != null) {
                        mAnimeClose.end();
                    }
                    mAnimeOpen.start();
                } else {
                    if (mAnimeClose == null) {
                        mAnimeClose = ValueAnimator.ofInt(mMaxHeight - mInitHeight);
                        mAnimeClose.setDuration(300);
                        mAnimeClose.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                int height = (int) animation.getAnimatedValue();
                                AppConfig.logI(mMaxHeight - height + "");
                                LayoutParams layoutParams = (LayoutParams) CustomTextView.this.getLayoutParams();
                                layoutParams.height = mMaxHeight - height;
                                CustomTextView.this.setLayoutParams(layoutParams);
                            }
                        });
                    }

                    if (mAnimeOpen != null) {
                        mAnimeOpen.end();
                    }
                    mAnimeClose.start();
                }
                mIsClose = !mIsClose;
            }

        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mTv.setMaxLines(Integer.MAX_VALUE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mTv.getLineCount() > mInitNum) {
            if (mIsClose) {
                mCount = mTv.getLineCount();
                mMaxHeight = getRealTextViewHeight(mTv, mCount);

                AppConfig.logI(mMaxHeight + "  mMaxHeight=");
            }
        } else {
            mMaxHeight = 0;
        }

        if (mInitHeight == 0) {
            mInitHeight = getRealTextViewHeight(mTv, mInitNum);
        }

        if (mIsClose) {
            mTv.setMaxLines(mInitNum);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            throw new IllegalArgumentException("最少包含textview");
        }

        mTv = (TextView) getChildAt(0);
    }

    /**
     * 获取控件高度
     *
     * @param textView
     * @return
     */
    private int getRealTextViewHeight(TextView textView, int count) {
//        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int textHeight = textView.getLayout().getLineTop(count);
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }


}
