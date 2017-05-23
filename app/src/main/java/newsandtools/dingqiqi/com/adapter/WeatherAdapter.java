package newsandtools.dingqiqi.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.mode.WeatherForecastMode;

/**
 * Created by dingqiqi on 2016/7/1.
 */
public class WeatherAdapter extends BaseAdapter {

    private Context mContext;
    private List<WeatherForecastMode> mList;

    public WeatherAdapter(Context mContext, List<WeatherForecastMode> mList) {
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
        Holder mHolder;

        if (convertView == null) {
            mHolder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.items_weather_layout, null, false);

            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.tv_weather = (TextView) convertView.findViewById(R.id.tv_weather);
            mHolder.tv_feng = (TextView) convertView.findViewById(R.id.tv_fengxiang);
            mHolder.tv_temperature = (TextView) convertView.findViewById(R.id.tv_temperature);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        mHolder.tv_time.setText(mList.get(position).getWeek() + "(" + mList.get(position).getDate().replaceAll("-", "/") + ")");
        mHolder.tv_weather.setText(mList.get(position).getType());
        mHolder.tv_feng.setText(mList.get(position).getFengxiang() + "(" + mList.get(position).getFengli() + ")");
        mHolder.tv_temperature.setText(mList.get(position).getHightemp() + "/" + mList.get(position).getLowtemp());

        return convertView;
    }

    private class Holder {
        private ImageView iv_icon;
        private TextView tv_time;
        private TextView tv_weather;
        //风向
        private TextView tv_feng;
        //温度
        private TextView tv_temperature;
    }
}
