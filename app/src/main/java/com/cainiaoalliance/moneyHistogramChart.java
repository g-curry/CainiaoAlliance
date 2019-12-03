package com.cainiaoalliance;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public class moneyHistogramChart extends AppCompatActivity {
    public BarChart barChart;

    //数据集合
    public ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
    private LinkedList<MessageBean> records = new LinkedList<>();
    public BarDataSet dataset;
    //Y轴值
    LinkedList<String> dates = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_histogram_chart);
        barChart = (BarChart) findViewById(R.id.mBarChart);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        //添加数据
        initEntriesData();
        //添加x轴值
        initXLableData();
        //设置柱状图显示属性
        show();
    }

    //添加数据
    public void initEntriesData() {
        dates = GlobalUtil.getInstance().databaseHelper.getAvailableDate();
        int xlab = 0;
        float numShow;
        for (String date : dates) {
            records = GlobalUtil.getInstance().databaseHelper.readMessage(date);
            numShow = (float) showYlab();
            entries.add(new BarEntry(numShow,xlab));
            Log.d("numshow", "initEntriesData: " + numShow);
            Log.d("xlab", "initEntriesData: " + xlab);
            xlab++;
        }
    }


    //添加x轴值
    public void initXLableData() {
        dates = GlobalUtil.getInstance().databaseHelper.getAvailableDate();
    }

    public void show() {
        //装载显示数据
        dataset = new BarDataSet(entries, "日期");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        //封装x轴数据
        BarData data = new BarData(dates, dataset);
        //右侧Y轴关闭
        barChart.getAxisRight().setEnabled(false);
        //x轴显示到下面
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置数据字体大小
        barChart.getAxisLeft().setTextSize(12f);
        //设置X轴字体大小
        barChart.getXAxis().setTextSize(9f);
        //设置字体显示角度
        barChart.getXAxis().setLabelRotationAngle(-66);
        //装载数据
        barChart.setData(data);
        barChart.animateX(2000);
        // X,Y轴同时缩放，false则X,Y轴单独缩放,默认false
        barChart.setPinchZoom(true);
        //设置右下角表文字
//        barChart.setDescription("日期");
        barChart.setDescription("");
        //设置文字字号
        barChart.setDescriptionTextSize(9f);
        //设置表文字颜色
        barChart.setDescriptionColor(Color.BLACK);
        barChart.setNoDataTextDescription("You need to provide data for the chart.");
        //设置比例图
        Legend mLegend = barChart.getLegend();
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);//设置显示位置
//        mLegend.setForm(Legend.LegendForm.LINE);//设置比例图的形状，默认是方形
        mLegend.setFormSize(12f);
        //设置是否显示比例图
        mLegend.setEnabled(false);
    }

    public double showYlab() {
        DecimalFormat df = new DecimalFormat(".00");
        double totalCost;
        double dftotalCost = 0;

        for (MessageBean record : records) {
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
