package newsandtools.dingqiqi.com.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by dingqiqi on 2016/6/15.
 */
public class MainPageAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private List<Fragment> mList;

    public MainPageAdapter(FragmentManager fm, Context mContext, List<Fragment> mList) {
        super(fm);
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }


}
