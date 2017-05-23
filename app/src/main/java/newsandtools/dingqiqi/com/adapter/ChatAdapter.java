package newsandtools.dingqiqi.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import newsandtools.dingqiqi.com.R;
import newsandtools.dingqiqi.com.mode.ChatMode;

/**
 * Created by dingqiqi on 2016/6/24.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private Context mContext;
    private List<ChatMode> mList;

    public ChatAdapter(Context mContext, List<ChatMode> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(mContext).inflate(R.layout.items_chat_left_layout, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.items_chat_right_layout, parent, false);
        }

        return new Holder(view);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {

//        if (position > 0) {
//            if (mList.get(position).getTime().compareTo(mList.get(position - 1).getTime()) > 60) {
//                holder.tv_time.setVisibility(View.VISIBLE);
//            } else {
//                holder.tv_time.setVisibility(View.GONE);
//            }
//        }
        //机器人说话的时候才判断
        if (getItemViewType(position) == 0) {
            if (mList.get(position).isHint()) {
                holder.ll_hint.setVisibility(View.VISIBLE);
            } else {
                holder.ll_hint.setVisibility(View.GONE);
            }
        }

        holder.tv_time.setText(mList.get(position).getTime().substring(10).trim());
        holder.tv_msg.setText(mList.get(position).getText());


    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tv_time;
        private TextView tv_msg;
        //历史消息提示
        private LinearLayout ll_hint;

        public Holder(View itemView) {
            super(itemView);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_message);
            ll_hint = (LinearLayout) itemView.findViewById(R.id.linear_hint);
        }
    }
}
