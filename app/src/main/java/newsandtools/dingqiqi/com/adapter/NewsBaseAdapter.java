package newsandtools.dingqiqi.com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.config.AppConfig;
import newsandtools.dingqiqi.com.mode.NewsMode;
import newsandtools.dingqiqi.com.util.FileUtil;
import newsandtools.dingqiqi.com.util.TimeUtil;

/**
 * Created by Administrator on 2016/6/20.
 */
public class NewsBaseAdapter extends BaseAdapter {

    private Context mContext;
    private List<NewsMode> mList;

    public NewsBaseAdapter(Context mContext, List<NewsMode> mList) {
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

    /*@Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }*/

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder mHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.items_news_layout,
                    null, false);

            mHolder = new Holder();

            mHolder.tv_tilte = (TextView) convertView.findViewById(R.id.tv_title);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            mHolder.iv_img = (ImageView) convertView.findViewById(R.id.img);

            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }

        if (position < mList.size()) {
            mHolder.tv_tilte.setText(mList.get(position).getTitle());
            mHolder.tv_time.setText(TimeUtil.StringToDate(mList.get(position).getTime()));
            mHolder.tv_author.setText(mList.get(position).getAuthor());

            if (mList.get(position).getImg() == null || "".equals(mList.get(position).getImg())) {
                mHolder.iv_img.setVisibility(View.GONE);
                mHolder.iv_img.setTag("");
            } else {
                mHolder.iv_img.setVisibility(View.VISIBLE);
                mHolder.iv_img.setTag(mList.get(position).getImg());

                Bitmap bitmap = FileUtil.showImage(mContext,mHolder.iv_img, mList.get(position).getImg(), new FileUtil.DownImgCallBack() {
                    @Override
                    public void downImgSuccess(ImageView imageView, Bitmap bitmap) {
                        AppConfig.logI("downImgSuccess " + (bitmap != null));
                        //判断Tag，防止图片错乱
                        if (position < mList.size() && mHolder.iv_img.getTag().equals(mList.get(position).getImg())) {
                            imageView.setBackground(new BitmapDrawable(bitmap));
                        }
                    }
                });

                if (bitmap != null) {
                    mHolder.iv_img.setBackground(new BitmapDrawable(bitmap));
                } else {
                    mHolder.iv_img.setBackgroundResource(R.mipmap.ic_launcher);
                }
            }
        }

        return convertView;
    }

    private class Holder {
        private TextView tv_tilte;
        private TextView tv_author;
        private TextView tv_time;
        private ImageView iv_img;
    }
}
