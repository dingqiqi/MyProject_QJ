package newsandtools.dingqiqi.com.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import newsandtools.dingqiqi.com.R;

public class TabButton extends View {
    /**
     * 颜色字体画笔
     */
    private Paint mPaint;
    /**
     * 黑色字体画笔
     */
    private Paint mPaintText;
    /**
     * 要画的字
     */
    private String mText = "";
    /**
     * 测量字
     */
    private Rect mTextBounds;
    /**
     * 画的图
     */
    private Bitmap mBitmap;
    /**
     * 图的左边距离
     */
    private int left;
    /**
     * 图的上边距离
     */
    private int top;
    /**
     * 图的宽度
     */
    private int mBitmapWidth;
    /**
     * 透明度
     */
    private int mAlpha = 0;
    /**
     * 颜色
     */
    private int mColor;

    /**
     * 设置透明度，最大的是255，设置180是为了颜色不那么亮
     *
     * @param alpha
     */
    public void setIconAlpha(float alpha) {
        this.mAlpha = (int) Math.ceil(220 * alpha);

        mPaint.setAlpha(mAlpha);

        invalidate();
    }

    public TabButton(Context context) {
        super(context, null);

    }

    public TabButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * 获取自定义属性
         */
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.test);

        float textSize = array.getDimension(R.styleable.test_textSize, 16);
        mText = array.getString(R.styleable.test_text);
        mColor = array.getColor(R.styleable.test_txtcolor, getResources().getColor(R.color.blue));
        /**
         * 获取图片
         */
        BitmapDrawable bitmapDrawable = (BitmapDrawable) array
                .getDrawable(R.styleable.test_tabicon);
        mBitmap = bitmapDrawable.getBitmap();

        array.recycle();

        /**
         * 初始化画笔
         */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(mColor);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(textSize);
        mPaintText.setColor(Color.BLACK);

        mTextBounds = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算图片宽度，高度
        mBitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingBottom()
                - getPaddingTop() - mTextBounds.height() * 2);
        //计算图片左边距，上边距
        left = getMeasuredWidth() / 2 - mBitmapWidth / 2;
        top = getMeasuredHeight() / 2 - mBitmapWidth / 2 - mTextBounds.height()
                / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //话底层图片
        canvas.drawBitmap(mBitmap, left, top, null);
        //获取上层图片
        Bitmap bitmap = drawSrcBitmap();
        //画上层图片，覆盖底层图片
        canvas.drawBitmap(bitmap, 0, 0, null);
        //话底层文字
        drawBgText(canvas);
        //画上层文字
        drawSrcText(canvas);
    }

    /**
     * 利用XferMode，获取到图片对应的形状
     *
     * @return
     */
    private Bitmap drawSrcBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(),
                getMeasuredHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mColor);
        paint.setAlpha(mAlpha);

        canvas.drawRect(left, top, left + mBitmap.getWidth(),
                top + mBitmap.getHeight(), paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

        canvas.drawBitmap(mBitmap, left, top, paint);

        return bitmap;
    }

    /**
     * 画黑色字
     *
     * @param canvas
     */
    private void drawBgText(Canvas canvas) {
        canvas.drawText(mText,
                getMeasuredWidth() / 2 - mTextBounds.width() / 2, top
                        + mBitmapWidth + mTextBounds.height(), mPaintText);
    }

    /**
     * 画带颜色字
     *
     * @param canvas
     */
    private void drawSrcText(Canvas canvas) {
        mPaint.setAlpha(mAlpha);
        canvas.drawText(mText,
                getMeasuredWidth() / 2 - mTextBounds.width() / 2, top
                        + mBitmapWidth + mTextBounds.height(), mPaint);
    }

}
