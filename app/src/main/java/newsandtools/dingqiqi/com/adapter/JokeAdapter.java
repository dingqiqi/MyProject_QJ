package newsandtools.dingqiqi.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.mode.JokeMode;
import newsandtools.dingqiqi.com.view.CustomTextView;

/**
 * Created by dingqiqi on 2016/6/22.
 */
public class JokeAdapter extends BaseAdapter {

    private Context mContext;
    private List<JokeMode> mList;

    public JokeAdapter(Context mContext, List<JokeMode> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder mHolder;

        if (convertView == null) {
            mHolder = new Holder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.items_joke_layout, null, false);

            mHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        if (position < mList.size()) {
            mHolder.tv_title.setText(mList.get(position).getTitle());
            mHolder.tv_content.setText(mList.get(position).getText());

            String time = mList.get(position).getCt().substring(0, 16).replaceAll("-", "/");
            mHolder.tv_time.setText(time);
        }

        return convertView;
    }

    private class Holder {
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;
    }
}
