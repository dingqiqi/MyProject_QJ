package newsandtools.dingqiqi.com.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * 圆形图片
 */
public class CircleImageView extends ImageView {

    private Paint mPaint;
    private Xfermode mXFerMode = new PorterDuffXfermode(Mode.DST_IN);
    private Bitmap mMaskBitmap;
    private int dWidth;
    private int dHeight;

    public CircleImageView(Context context) {
        this(context, null);
        initView();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initView() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(false);
        mPaint.setXfermode(mXFerMode);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(width, width);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // 拿到Drawable
        Drawable drawable = getDrawable();

        if (drawable != null) {
            // 按照bitmap的宽高，以及view的宽高，计算缩放比例；因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真；
            dWidth = drawable.getIntrinsicWidth();
            dHeight = drawable.getIntrinsicHeight();

            float mScale = 1.0f;
            mScale = Math.max(getMeasuredWidth() * 1.0F / dWidth, getMeasuredHeight() * 1.0F / dHeight);
            // 根据缩放比例，设置bounds，相当于缩放图片了
            dWidth = (int) (mScale * dWidth);
            dHeight = (int) (mScale * dHeight);

            //为了取到中间的图
            Bitmap bitmap = getBitmap(false, drawable);
            //压缩图片
            //            Matrix matrix = new Matrix();
            //            matrix.setScale(mScale, mScale);
            //            bitmap1 = Bitmap.createBitmap(bitmap1, bitmap1.getWidth() / 2 - getMeasuredWidth() / 2, bitmap1.getHeight() / 2 - getMeasuredHeight() / 2, getMeasuredWidth(), getMeasuredHeight(), matrix, true);
            //画图片
            canvas.drawBitmap(bitmap, (getMeasuredWidth() - dWidth) / 2, (getMeasuredHeight() - dHeight) / 2, null);
//            canvas.drawBitmap(bitmap, 0, 0, null);
            //获取圆形
            mMaskBitmap = getBitmap(true, null);
            // 绘制圆形
            canvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
        }

    }

    /**
     * 绘制形状
     *
     * @return
     */
    public Bitmap getBitmap(boolean isCenter, Drawable drawable) {
        Bitmap bitmap;
        //产生圆形bitmap
        if (drawable == null) {
            bitmap = Bitmap.createBitmap(dWidth, dHeight,
                    Config.ARGB_4444);
        } else {
            //按照图片大小产生bitmap
            drawable.setBounds(0, 0, dWidth, dHeight);
            bitmap = Bitmap.createBitmap(dWidth, dHeight,
                    Config.ARGB_4444);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        if (isCenter) {
            int radius = Math.min(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, paint);
        } else {
            drawable.draw(canvas);
        }

        return bitmap;
    }

}
