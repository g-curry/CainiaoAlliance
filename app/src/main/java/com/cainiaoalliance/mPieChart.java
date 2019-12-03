package com.cainiaoalliance;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.LinkedList;

public class mPieChart extends AppCompatActivity {

    public PieChart mChart;
    private LinkedList<MessageBean> records = new LinkedList<>();
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_pie_chart);

        Intent intent = getIntent();
        String action = intent.getAction();
        if(action.equals("one")){
            date = intent.getStringExtra("key");
            Log.e("Intent传值", "onCreate: -----------OneActivity:"+ date );
        }
        records = GlobalUtil.getInstance().databaseHelper.readMessage(date);
        int count = records.size();

        mChart = (PieChart) findViewById(R.id.mPieChart);
        PieData mPieData = getPieData(count, 100);
        showChart(mChart, mPieData);
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);
        //半径
        pieChart.setHoleRadius(40f);
        //半透明圈
        pieChart.setTransparentCircleRadius(44f);
        //pieChart.setHoleRadius(0)//实心圆
        //饼状图中间可以添加文字
        pieChart.setDrawCenterText(true);
        pieChart.setDrawHoleEnabled(true);
        //初始旋转角度
        pieChart.setRotationAngle(90);
        //可以手动旋转
        pieChart.setRotationEnabled(true);
        //显示成百分比
        pieChart.setUsePercentValues(true);
        int count = records.size();
        if (count != 0){
            //饼状图中间的文字
            pieChart.setCenterText("销售种类及占比");
        }
        else {
            pieChart.setCenterText("");
        }
        pieChart.setCenterTextColor(Color.BLUE);
        pieChart.setCenterTextSize(15f);
        pieChart.setCenterTextSizePixels(40f);
        //设置数据
        pieChart.setData(pieData);
        //显示日期
        pieChart.setDescription(date);
        //设置文字字号
        pieChart.setDescriptionTextSize(20f);
        //设置比例图
        Legend mLegend = pieChart.getLegend();
        //最右边显示
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);
        //设置动画
        pieChart.animateXY(1000, 1000);
    }
    //分成几部分
    private PieData getPieData(int count, float range) {
        records = GlobalUtil.getInstance().databaseHelper.readMessage(date);
        count = records.size();
        ArrayList<String> xValues = new ArrayList<String>();
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        int record_size = 0;
        double yData;
        for (MessageBean record : records) {

            xValues.add(record.getName());
            yData = Double.parseDouble(record.getSum());
            yValues.add(new Entry((float) yData, record_size));
            record_size++;
        }

//        double yData1;
//        double yGetData;
//        for (MessageBean record : records) {
//            for(int i=0; i < xValues.size(); i++) {
//                if (xValues.get(i) != record.getName()){
//                    xValues.add(record.getName());
//                    yData = Double.parseDouble(record.getSum());
//                    yValues.add(new Entry((float) yData, record_size));
//                }
//                else{
//                    yGetData = Double.parseDouble(String.valueOf(yValues.get(i)));
//                    yData1 = Double.parseDouble(record.getSum()) + yGetData;
//                    yValues.add(new Entry((float) yData1, record_size));
//                }
//            }
//            record_size++;
//        }

        //y轴的集合/*显示在比例图上*/
        PieDataSet pieDataSet = new PieDataSet(yValues,"今日销售种类");
        //设置个饼状图之间的距离
        pieDataSet.setSliceSpace(1f);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //设置圆盘文字颜色
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(15f);
        //设置是否显示区域百分比的值
        //设置数据样式
        pieDataSet.setValueFormatter(new ValueFormatter()
        { @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler)
        { return ""+(int)value+"%"; }
        });
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        // 选中态多出的长度
        pieDataSet.setSelectionShift(px);
        PieData pieData = new PieData(xValues,pieDataSet);
        return pieData;
    }
}
