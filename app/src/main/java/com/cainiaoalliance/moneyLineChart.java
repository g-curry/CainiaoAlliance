package com.cainiaoalliance;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;

public class moneyLineChart extends AppCompatActivity {

    public LineChart lineChart;

    //数据集合
    public ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
    private LinkedList<MessageBean> records = new LinkedList<>();
    public BarDataSet dataset;
    //Y轴值
    LinkedList<String> dates = new LinkedList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_line_chart);
        lineChart = (LineChart) findViewById(R.id.mLineChart);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        LineData mLineData = getLineData();
        showChart(lineChart, mLineData, Color.GRAY);
    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框
        // no description text
        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.BLACK); // 表格的的颜色，在这里是是给颜色设置一个透明度
        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放
        lineChart.setPinchZoom(true);
//        lineChart.setBackgroundColor(getResources().getColor(R.color.bj));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            lineChart.setBackground(getResources().getDrawable(R.drawable.banner1));// 设置背景
//        }
        lineChart.setData(lineData); // 设置数据
        lineChart.getXAxis().setAxisLineColor(Color.BLACK);
        lineChart.getXAxis().setGridColor(Color.GRAY);
        lineChart.getAxisRight().setGridColor(Color.GRAY);
        lineChart.getXAxis().setTextSize(10f);
        lineChart.getXAxis().setTextColor(Color.BLACK);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);//x轴显示到下面
        lineChart.getRendererLeftYAxis().getPaintAxisLine().setColor(Color.BLACK);
        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(15f);// 字体
        mLegend.setTextColor(Color.RED);// 颜色
        lineChart.animateX(2500); // 立即执行的动画,x轴
    }

    /**
     * X Y 轴数据获取
     *
     */
    private LineData getLineData() {

        //X轴数据
        dates = GlobalUtil.getInstance().databaseHelper.getAvailableDate();
        ArrayList<String> xValues = new ArrayList<String>();
        for (String date : dates) {
            xValues.add(date);
        }

        //Y轴数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        int xlab = 0;
        float numShow;
        for (String date : dates) {
            records = GlobalUtil.getInstance().databaseHelper.readMessage(date);
            numShow = (float) showYlab();
            yValues.add(new Entry(numShow, xlab));
            entries.add(new BarEntry(numShow,xlab));
            Log.d("numshow", "initEntriesData: " + numShow);
            Log.d("xlab", "initEntriesData: " + xlab);
            xlab++;
        }

        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "销售额折线图 " /*显示在比例图上*/);
        lineDataSet.setValueTextColor(Color.RED);
        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(2f); //线宽
        lineDataSet.setCircleSize(4f);//显示的圆形大小
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setColor(Color.RED);//显示颜色
        lineDataSet.setCircleColor(Color.RED);//圆形的颜色
        lineDataSet.setHighLightColor(Color.CYAN); //高亮的线的颜色
        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); //添加数据
        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
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
