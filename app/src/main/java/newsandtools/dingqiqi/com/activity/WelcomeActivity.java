package newsandtools.dingqiqi.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import newsandtools.dingqiqi.com.R;

/**
 * Created by dingqiqi on 2016/6/14.
 */
public class WelcomeActivity extends Activity {

    private ViewPager mPager;

    private List<View> mList;
    /**
     * 小红点
     */
    private ImageView mRedPoint;

    private int mPointWidth = 0;

    private LinearLayout mLayout;

    private int[] str = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c,
            R.mipmap.d};
    /**
     * 进入应用
     */
    private TextView mTvInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_layout);

        mList = new ArrayList<>();
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mRedPoint = (ImageView) findViewById(R.id.iv_readpoint);
        mLayout = (LinearLayout) findViewById(R.id.linear);

        mTvInsert = (TextView) findViewById(R.id.tv_insert);

        // 加载出来
        mRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mPointWidth = mLayout.getChildAt(1).getLeft()
                                - mLayout.getChildAt(0).getLeft();
                    }
                });

        for (int i = 0; i < str.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(str[i]);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            view.setLayoutParams(layoutParams);
            mList.add(view);

            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.shape_oval_gray);

            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (i != 0) {
                params.leftMargin = 20;
            }

            imageView.setLayoutParams(params);
            mLayout.addView(imageView);
        }

        mPager.setAdapter(new CustomPager());
        mPager.setOnPageChangeListener(mChangeListener);

        mTvInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class CustomPager extends PagerAdapter {

        @Override
        public int getCount() {

            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            // super.destroyItem(container, position, object);
            container.removeView(mList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(mList.get(position));
            return mList.get(position);
        }

    }

    private ViewPager.OnPageChangeListener mChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == mList.size() - 1) {
                mTvInsert.setVisibility(View.VISIBLE);
            } else {
                mTvInsert.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // 正在滑动
            LayoutParams params = (LayoutParams) mRedPoint.getLayoutParams();
            params.leftMargin = Math.round((arg0 + arg1) * mPointWidth);
            mRedPoint.setLayoutParams(params);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}
