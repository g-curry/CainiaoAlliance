package com.cainiaoalliance;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * @author g_curry   2019/5/28 21:23
 */
public class MessageListViewAdapter extends BaseAdapter {

    private LinkedList<MessageBean> records = new LinkedList<>();
    private LayoutInflater mInflater;
    private Context mContext;

    public MessageListViewAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(LinkedList<MessageBean> records) {
        this.records = records;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cell_list_view, null);
            MessageBean recordBean = (MessageBean) getItem(position);
            holder = new ViewHolder(convertView, recordBean);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
}


class ViewHolder {
    TextView messageid_tv, timeStamp_tv, sum_tv,amount_tv,name_tv;

    public ViewHolder(View itemView, MessageBean record) {
        messageid_tv = itemView.findViewById(R.id.message_id_tv);
        timeStamp_tv = itemView.findViewById(R.id.timeStamp_tv);
        sum_tv = itemView.findViewById(R.id.sum_tv);
        amount_tv = itemView.findViewById(R.id.amount_text);
        name_tv = itemView.findViewById(R.id.message_name_tv);

        messageid_tv.setText(record.getMessageid());
        timeStamp_tv.setText(record.getTimeStamp());
        sum_tv.setText(record.getSum());
        name_tv.setText(record.getName()+","+record.getName()+","+record.getName()+","+record.getName());
        name_tv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线
    }
}



