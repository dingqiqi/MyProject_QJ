package newsandtools.dingqiqi.com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import newsandtools.dingqiqi.com.util.DensityUtil;

/**
 * Created by Administrator on 2016/6/16.
 */
public class MarqueeView extends TextView {

    private Paint mPaint;

    private Context mContext;

    private int mWidth;
    private int mHeight;

    private int mCurrentWidth;

    private Handler mHandler;
    //渐变颜色
    private String[] colors = new String[]{"#600030", "#820041", "#9F0050", "#BF0060", "#D9006C", "#F00078", "#FF0080", "#FF359A", "#FF60AF", "#FF79BC", "#FF95CA", "#ffaad5"};

    private int mCount = 0;

    public MarqueeView(Context context) {
        super(context, null);

    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mPaint = new Paint();

        mHandler = new Handler();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeCallbacks(mRunnable);
                break;
            case MotionEvent.ACTION_UP:
                mHandler.postDelayed(mRunnable, 30);
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        mPaint.setTextSize(getTextSize());
        mPaint.setColor(Color.parseColor(colors[mCount]));
        mPaint.setAntiAlias(true);
//        mPaint.setUnderlineText(true);

        Rect textBounds = new Rect();
        mPaint.getTextBounds(getText().toString(), 0, getText().length(), textBounds);

        int height = mHeight * 2 / 3;

        canvas.drawText(getText().toString(), mCurrentWidth, height, mPaint);

        mCurrentWidth += 3;
        mCount++;

        if (mCount == colors.length - 1) {
            mCount = 0;
        }

        if (mCurrentWidth >= mWidth) {
            mCurrentWidth = 0;
        }

        mHandler.postDelayed(mRunnable, 30);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

    }
}
