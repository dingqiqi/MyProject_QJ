package newsandtools.dingqiqi.com.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.adapter.MainPageAdapter;
import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.fragment.FirstFragment;
import newsandtools.dingqiqi.com.fragment.SecondFragment;
import newsandtools.dingqiqi.com.fragment.ThirdFragment;
import newsandtools.dingqiqi.com.fragment.FourchFragment;
import newsandtools.dingqiqi.com.util.FileUtil;
import newsandtools.dingqiqi.com.view.CustomViewpager;
import newsandtools.dingqiqi.com.view.DrawLayout;
import newsandtools.dingqiqi.com.view.TabButton;

/**
 * 主界面
 * Created by dingqiqi on 2016/6/14.
 */
public class MainActivity extends BaseActivity {
    /**
     * 自防侧滑菜单
     */
    private DrawLayout mDrawLayout;
    /**
     * 可改变能否滑动的Viewpager
     */
    private CustomViewpager mViewpager;
    /**
     * 滑动变色按钮集合
     */
    private List<TabButton> mList;
    /**
     * 页面集合
     */
    private List<Fragment> mListFragment;
    /**
     * Viewpager适配器
     */
    private MainPageAdapter mAdapter;
    /**
     * 图片下载设置
     */
    private TextView mTvDownImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        mDrawLayout = (DrawLayout) findViewById(R.id.drawlayout);
        mTvDownImg = (TextView) findViewById(R.id.tv_down_img);
        //读取配置
        FileUtil.readDownImageEnable(this);

        mListFragment = new ArrayList<>();
        FirstFragment newsFragment = new FirstFragment();
        SecondFragment secondFragment = new SecondFragment();
        ThirdFragment thirdFragment = new ThirdFragment();
        FourchFragment fourchFragment = new FourchFragment();

        mListFragment.add(newsFragment);
        mListFragment.add(secondFragment);
        mListFragment.add(thirdFragment);
        mListFragment.add(fourchFragment);

        mViewpager = (CustomViewpager) findViewById(R.id.viewpager);
        mAdapter = new MainPageAdapter(getSupportFragmentManager(), this, mListFragment);
        mViewpager.setAdapter(mAdapter);
        mViewpager.setOffscreenPageLimit(3);
        mViewpager.setOnPageChangeListener(mOnPageChangeListener);

        mList = new ArrayList<TabButton>();

        TabButton btn1 = (TabButton) findViewById(R.id.tabbtn1);
        TabButton btn2 = (TabButton) findViewById(R.id.tabbtn2);
        TabButton btn3 = (TabButton) findViewById(R.id.tabbtn3);
        TabButton btn4 = (TabButton) findViewById(R.id.tabbtn4);

        mList.add(btn1);
        mList.add(btn2);
        mList.add(btn3);
        mList.add(btn4);

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        btn4.setOnClickListener(listener);
        mTvDownImg.setOnClickListener(listener);

        mList.get(0).setIconAlpha(1);

        mDrawLayout.setmPageChangeListener(new DrawLayout.PageChangeListener() {
            @Override
            public void OpenOrClose(boolean isOpen) {
                AppConfig.logI("DrawLayout-->状态：" + isOpen);
                if (isOpen) {
                    mViewpager.setIsScroll(false);
                    //第一页
                    if (mViewpager.getCurrentItem() == 0) {
                        if (mListFragment.get(0) instanceof FirstFragment) {

                            if (((FirstFragment) mListFragment.get(0)).mViewPager != null) {
                                ((FirstFragment) mListFragment.get(0)).mViewPager.setIsScroll(false);
                            }
                        }
                    }
                } else {
                    mViewpager.setIsScroll(true);

                    //第一页
                    if (mViewpager.getCurrentItem() == 0) {

                        if (mListFragment.get(0) instanceof FirstFragment) {
                            //第一页
                            if (((FirstFragment) mListFragment.get(0)).mViewPager != null) {
                                ((FirstFragment) mListFragment.get(0)).mViewPager.setIsScroll(true);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 控制Drawlayout是否能打开
     */
    public void setDrawLayoutIsOpen(boolean flag) {
        mDrawLayout.setmIsCanScroll(flag);
    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tabbtn1:
                    initBtn(0);
                    break;
                case R.id.tabbtn2:
                    initBtn(1);
                    break;
                case R.id.tabbtn3:
                    initBtn(2);
                    break;
                case R.id.tabbtn4:
                    initBtn(3);
                    break;
                case R.id.tv_down_img:
                    if (FileUtil.mDownImageEnable) {
                        FileUtil.saveDownImageEnable(MainActivity.this, false);
                        mTvDownImg.setText("无图模式");
                    } else {
                        FileUtil.saveDownImageEnable(MainActivity.this, true);
                        mTvDownImg.setText("有图模式");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void initBtn(int index) {
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setIconAlpha(0);
        }

        mList.get(index).setIconAlpha(1);
        mViewpager.setCurrentItem(index, false);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == 0) {
                mDrawLayout.setmIsCanScroll(true);
            } else {
                mDrawLayout.setmIsCanScroll(false);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

            //不等于
            if (arg1 > 0) {
                mList.get(arg0).setIconAlpha(1 - arg1);
                mList.get(arg0 + 1).setIconAlpha(arg1);
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mViewpager.getCurrentItem() == 0) {
                mDrawLayout.OpenOrClose();
            }
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_BACK) {

            AppConfig.showExit(this);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
