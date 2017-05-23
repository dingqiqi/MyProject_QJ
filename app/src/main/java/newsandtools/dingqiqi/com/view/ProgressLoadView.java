package newsandtools.dingqiqi.com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.util.DensityUtil;

/**
 * Created by dingqiqi on 2016/6/16.
 */
public class ProgressLoadView extends View {
    /**
     * 进度条半径
     */
    private int mRadious = 40;
    /**
     * 默认字体大小
     */
    private int mSize = 14;
    /**
     * 默认字体颜色
     */
    private int mColorText;
    /**
     * 默认进度条颜色
     */
    private int mColorPro;
    /**
     * 控件宽高
     */
    private int mWidth;
    private int mHeight;
    /**
     * 字体画笔
     */
    private Paint mPaintTxt;
    /**
     * 进度条画笔
     */
    private Paint mPaintPro;
    /**
     * 默认字
     */
    private String mText = "加载中...";

    private int mAngle = 0;

    public ProgressLoadView(Context context) {
        super(context, null);
    }

    public ProgressLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mColorPro = getResources().getColor(R.color.colorPrimary);
        mColorText = getResources().getColor(R.color.white);

        mPaintTxt = new Paint();
        mPaintTxt.setAntiAlias(true);
        mPaintTxt.setTextSize(DensityUtil.dip2px(context, mSize));
        mPaintTxt.setColor(mColorText);
        mPaintTxt.setTextAlign(Paint.Align.CENTER);

        mPaintPro = new Paint();
        mPaintPro.setAntiAlias(true);
        mPaintPro.setColor(mColorPro);
        mPaintPro.setStrokeWidth(8);
        mPaintPro.setStyle(Paint.Style.STROKE);

        mRadious = DensityUtil.dip2px(context, mRadious);
    }

    /**
     * 设置加载文字
     *
     * @param str
     */
    public void setText(String str) {
        mText = str;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        RectF rectF = new RectF(-mRadious / 2, -mRadious - mRadious / 4, mRadious / 2, -mRadious / 4);

        mPaintPro.setColor(getResources().getColor(R.color.colorPrimaryProBar));
        canvas.drawCircle(0, -mRadious * 3 / 4, mRadious / 2, mPaintPro);

        mPaintPro.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawArc(rectF, 0, mAngle, false, mPaintPro);

        canvas.drawText(mText, 0, mRadious / 4, mPaintTxt);

        mAngle += 5;

        if (mAngle > 360) {
            mAngle = 0;
        }

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

    }
}
