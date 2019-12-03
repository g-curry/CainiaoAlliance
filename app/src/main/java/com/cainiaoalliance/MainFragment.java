package com.cainiaoalliance;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * @author g_curry   2019/5/28 23:17
 */
public class MainFragment extends Fragment {

    private View rootView;
    private TextView mTextView;
    private ListView mListView;
    private MessageListViewAdapter messageListViewAdapter;

    private LinkedList<MessageBean> records = new LinkedList<>();
    private String date = null;

    @SuppressLint("ValidFragment")
    public MainFragment(String date) {

        if(date != null)this.date = date;
        records = GlobalUtil.getInstance().databaseHelper.readMessage(date);

        Log.d("MainFragment","===========");
        Log.d("records", String.valueOf(records));

    }

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        return rootView;
    }

//    当数据更新时重载界面
    public void reload() {
        records = GlobalUtil.getInstance().databaseHelper.readMessage(date);
        if (messageListViewAdapter == null) {
            messageListViewAdapter = new MessageListViewAdapter(getActivity().getApplicationContext());
        }
        messageListViewAdapter.setData(records);
        mListView.setAdapter(messageListViewAdapter);

        if (messageListViewAdapter.getCount() > 0) {
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }
    }

    //初始化界面
    private void initView() {
        mTextView = rootView.findViewById(R.id.day_text);
        mListView = rootView.findViewById(R.id.listView);
        mTextView.setText(date);
        messageListViewAdapter = new MessageListViewAdapter(getContext());
        messageListViewAdapter.setData(records);
        mListView.setAdapter(messageListViewAdapter);

        if (messageListViewAdapter.getCount() > 0) {
            rootView.findViewById(R.id.no_record_layout).setVisibility(View.INVISIBLE);
        }
//        mTextView.setText(DateUtil.getDateTitle(date));
//        mListView.setOnItemLongClickListener(this);
    }

    public double getTotalCost() {
        DecimalFormat df = new DecimalFormat(".00");
        double totalCost;
        double dftotalCost = 0;

        for (MessageBean record : records) {
            Log.d("getTotalCost","===========");
            Log.d("records", String.valueOf(records));
//            dftotalCost += Double.parseDouble(record.getSum());
            if(record.getVip().length() != 2 ){
                double sumbefore = Double.parseDouble(record.getSum());
                double sumnow = Double.parseDouble(df.format(sumbefore * 0.9));
                String sumshow = new String(String.valueOf(sumnow));
                dftotalCost += Double.parseDouble(sumshow);
            }
            else{
                dftotalCost += Double.parseDouble(record.getSum());
            }
        }
        totalCost = Double.parseDouble(df.format(dftotalCost));//显示小数点后两位
        return totalCost;
    }
}
