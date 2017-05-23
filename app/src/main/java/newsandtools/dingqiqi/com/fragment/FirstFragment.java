package newsandtools.dingqiqi.com.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.activity.MainActivity;
import newsandtools.dingqiqi.com.adapter.NewPageAdapter;
import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.util.DensityUtil;
import newsandtools.dingqiqi.com.util.HttpUtil;
import newsandtools.dingqiqi.com.view.CustomFragmentViewpager;

/**
 * Created by dingqiqi on 2016/6/15.
 */
public class FirstFragment extends Fragment {
    /**
     * 是否第一次启动
     */
    private boolean mIsPrepared;
    /**
     * 是否显示
     */
    private boolean mIsVisibleHint;
    /**
     * 标题栏
     */
    private RadioGroup mRadioGroup;
    /**
     * 滑动页面
     */
    public CustomFragmentViewpager mViewPager;
    /**
     * 网络请求
     */
    private HttpURLConnection mConnection;
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * radiobutton间距
     */
    private int mMargin = 20;
    /**
     * 选中的频道
     */
    public int mCurrentIndex = 0;
    /**
     * 适配器
     */
    private NewPageAdapter mPageAdapter;
    /**
     * 新闻页面数据源
     */
    public List<NewsFragment> mList;
    /**
     * radiobutton 宽度
     */
    private int mWidthRb;
    /**
     * 提示view
     */
    private View mHintView;
    /**
     * 包裹标题栏,为了滑动
     */
    private HorizontalScrollView mScrollView;

    private MainActivity mActivity;
    /**
     * viewpager是否在滑动
     */
    private boolean mIsScroll = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragement_first_layout, null, false);

        initView(mView);
        setListener();

        mList = new ArrayList<>();
        mActivity = (MainActivity) getActivity();
        mIsPrepared = true;

        return mView;
    }

    /**
     * 初始化布局
     *
     * @param mView
     */
    private void initView(View mView) {
        mViewPager = (CustomFragmentViewpager) mView.findViewById(R.id.viewpager1);
        mRadioGroup = (RadioGroup) mView.findViewById(R.id.rg);
        mHintView = mView.findViewById(R.id.view_hint);
        mScrollView = (HorizontalScrollView) mView.findViewById(R.id.scrollview);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.getChildAt(checkedId);
                AppConfig.logI("状态：" + checkedId + "-->" + radioButton.isChecked());

                //将之前选中的清空
                if (mCurrentIndex != checkedId) {
                    ((RadioButton) mRadioGroup.getChildAt(mCurrentIndex)).setTextColor(getResources().getColor(R.color.translate_white));
                    mCurrentIndex = checkedId;
                }

                radioButton.setTextColor(getResources().getColor(R.color.white));
                mViewPager.setCurrentItem(checkedId);
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIsPrepared = true;
        setUserVisibleHint(mIsVisibleHint);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleHint = isVisibleToUser;

        if (getUserVisibleHint() && mIsPrepared) {
            setHintView();
            RequestData();
        } else {
            CancelData();
        }

    }

    /**
     * 取消请求
     */
    private void CancelData() {
//        AppConfig.logE(" first cancel");
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }

    /**
     * 请求数据
     */
    private void RequestData() {
        AppConfig.logE("first request");

        //判断是否已经加载
        if (mRadioGroup.getChildCount() > 0) {
            mRadioGroup.removeAllViews();
        }

        for (int i = 0; i < HttpUtil.getChannelList(getActivity()).size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setId(i);

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
            if (i == 0) {
                layoutParams.leftMargin = DensityUtil.dip2px(getActivity(), mMargin) / 2;
            } else {
                layoutParams.leftMargin = DensityUtil.dip2px(getActivity(), mMargin);
            }

            if (i == HttpUtil.getChannelList(getActivity()).size() - 1) {
                layoutParams.rightMargin = DensityUtil.dip2px(getActivity(), mMargin) / 2;
            }

            radioButton.setTextColor(getResources().getColor(R.color.translate_white));
            radioButton.setText(HttpUtil.getChannelList(getActivity()).get(i).getName());
            radioButton.setTextSize(16);
            Bitmap bitmap = null;
            radioButton.setButtonDrawable(new BitmapDrawable(bitmap));

            radioButton.setLayoutParams(layoutParams);

            mRadioGroup.addView(radioButton);
        }

        ((RadioButton) mRadioGroup.getChildAt(mCurrentIndex)).setChecked(true);

        mList.clear();
        for (int i = 0; i < HttpUtil.getChannelList(getActivity()).size(); i++) {
            NewsFragment newsFragment = new NewsFragment();

            Bundle bundle = new Bundle();
            bundle.putString("value", HttpUtil.getChannelList(getActivity()).get(i).getValue());
            newsFragment.setArguments(bundle);

            mList.add(newsFragment);
        }

        mPageAdapter = new NewPageAdapter(getChildFragmentManager(), mList);
//        mPageAdapter = new NewPageAdapter(getActivity().getSupportFragmentManager(), mList);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setIsScroll(true);
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.setCurrentItem(mCurrentIndex);

        //获取radiobutton控件长度
        mRadioGroup.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //第一次设置初始位置以及宽度
                if (mWidthRb == 0) {
                    if (mRadioGroup.getChildCount() > 0) {
                        mWidthRb = ((RadioButton) mRadioGroup.getChildAt(0)).getWidth();
                        ((RadioButton) mRadioGroup.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
                        setHintView();
                    }
                }

                return true;
            }
        });
    }


    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset > 0) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mHintView.getLayoutParams();
                layoutParams.leftMargin = Math.round((position + positionOffset) * (mWidthRb + DensityUtil.dip2px(getActivity(), mMargin))
                        + DensityUtil.dip2px(getActivity(), mMargin) / 2);
                layoutParams.width = mWidthRb;
                mHintView.setLayoutParams(layoutParams);
            }

            mScrollView.scrollTo(Math.round((position - 3 + positionOffset) * (mWidthRb + DensityUtil.dip2px(getActivity(), mMargin))), 0);
        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mRadioGroup.getChildAt(position);
            radioButton.setTextColor(getResources().getColor(R.color.white));

            //不是同一个（最开始选中的时候）
            if (mCurrentIndex != position) {
                //将之前选中的清空
                ((RadioButton) mRadioGroup.getChildAt(mCurrentIndex)).setTextColor(getResources().getColor(R.color.translate_white));
                mCurrentIndex = position;
            }

            if (position != 0) {
                mActivity.setDrawLayoutIsOpen(false);
            } else {
                mActivity.setDrawLayoutIsOpen(true);
            }

            mCurrentIndex = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            AppConfig.logI("" + state);
            switch (state) {
                case 1:
                    break;
                case 2:
                    mIsScroll = true;
                    break;
                case 0: {
                    if (!mIsScroll) {
                        if (mViewPager.getCurrentItem() == mList.size() - 1) {
                            mViewPager.setIsScroll(false);
                        }
                    }
                    mIsScroll = false;
                    break;
                }
            }


        }
    };

    /**
     * 设置滑动条宽度及未知
     */
    private void setHintView() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mHintView.getLayoutParams();
        layoutParams.width = mWidthRb;
        layoutParams.leftMargin = Math.round((mCurrentIndex) * (mWidthRb + DensityUtil.dip2px(getActivity(), mMargin))
                + DensityUtil.dip2px(getActivity(), mMargin) / 2);
        mHintView.setLayoutParams(layoutParams);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppConfig.logI("onDestroyView");
        CancelData();

        mCurrentIndex = 0;
        mIsScroll = false;
        mConnection = null;

        mViewPager.removeAllViews();
        mViewPager.destroyDrawingCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppConfig.logI("onDestroy");
        CancelData();

        mCurrentIndex = 0;
        mIsScroll = false;
        mConnection = null;

        mViewPager.removeAllViews();
        mViewPager.destroyDrawingCache();
    }
}
