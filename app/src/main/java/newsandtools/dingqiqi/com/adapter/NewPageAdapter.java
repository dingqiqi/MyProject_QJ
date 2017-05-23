package newsandtools.dingqiqi.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import newsandtools.dingqiqi.com.fragment.NewsFragment;

/**
 * Created by dingqiqi on 2016/6/17.
 */
public class NewPageAdapter extends FragmentPagerAdapter {

    private List<NewsFragment> mList;

    public NewPageAdapter(FragmentManager fm, List<NewsFragment> mList) {
        super(fm);
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
