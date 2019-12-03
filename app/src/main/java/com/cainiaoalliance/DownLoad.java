package com.cainiaoalliance;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cainiaoalliance.Mysql.DBUtils;
import com.cainiaoalliance.Mysql.MySqlDataBean;

import org.apache.commons.collections4.CollectionUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author g_curry
 *
 */

public class DownLoad extends AppCompatActivity {

    boolean ConnectFlage=true;
    int ShowPointSum=0;//显示点的数量,连接中.....(后面的点)
    boolean isConnect=true;//连接还是断开

    Button ConnectButton;//定义连接按钮
    Button SendButton;//定义发送按钮
    Button btn_insert_data;//定义插入数据按钮
    EditText IPEditText;//定义ip输入框
    EditText PortText;//定义端口输入框
    ProgressBar progressBar21;//进度条
    EditText Month_ET;//定义更新数据的月份
    EditText Day_ET;//定义更新数据的日期

    Socket socket = null;//定义socket
    private OutputStream outputStream=null;//定义输出流
    private InputStream inputStream=null;//定义输入流

    long exitTime=0;//返回按键计时
    private static int BANNER_VISIBLE = 1;

    private LinkedList<PriceBean> listprice = new LinkedList<>();
    private LinkedList<MessageBean> records = new LinkedList<>();

    /**
     * 将从单片机得到的数据进行分割，转换成正确的格式，依次存入进数据库
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == BANNER_VISIBLE) {
                String str =msg.obj.toString();

                String[] hang = str.split("\\;");
                String[][] lie = new String[hang.length][];
                for(int i=0;i<hang.length;i++){
                    lie[i] = hang[i].split(",");
                }

                Log.d("eeeeeeeeeeeee", " =      ");

                for(int i=0; i < lie.length; i++) {
                    String idStr =lie[i][0];
                    Log.d("idStr", idStr);
                    Log.d("idlength", String.valueOf(idStr.length()));
                }

                try {
                        for(int i=0; i < lie.length; i++){
                            String messageidStr =lie[i][0];
                            Log.d("messageidStr", messageidStr);
                            Log.d("messageidlength", String.valueOf(messageidStr.length()));
                            String priceStr =lie[i][2];
                            Log.d("priceStr", priceStr);
                            Log.d("priceStrlength", String.valueOf(priceStr.length()));
                            String weightStr =lie[i][3];
                            Log.d("weightStr", weightStr);
                            Log.d("weightStrlength", String.valueOf(weightStr.length()));
//                            String sumStr =lie[i][4];

                            DecimalFormat df = new DecimalFormat(".00");

                            Double sumstr1 = Double.parseDouble(priceStr) * Double.parseDouble(weightStr);
                            String sumStr = String.valueOf(Double.parseDouble(df.format(sumstr1)));
                            Log.d("sumStr", sumStr);
                            String dataStr = DateUtil.getFormattedDateA(Long.parseLong(messageidStr));
                            Log.d("dataStr", dataStr);
                            String timeStr = DateUtil.getFormattedTime(Long.parseLong(messageidStr));
                            Log.d("timeStr", timeStr);

                            MessageBean record = new MessageBean();
                            record.setMessageid(messageidStr);
                            record.setName(lie[i][1]);
                            record.setPrice(priceStr);
                            record.setWeight(weightStr);
                            record.setSum(sumStr);
                            record.setVip(lie[i][5]);
                            record.setDate(dataStr);
                            record.setTimeStamp(timeStr);
                            GlobalUtil.getInstance().databaseHelper.addMessage(record);
                        }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    };

    /**
     * 设置监听  插入数据监听器
     * 将数据上传到MySql数据库
     */
    private void setListener() {
        // 按钮点击事件
        btn_insert_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个线程来连接数据库并获取数据库中对应表的数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        records = GlobalUtil.getInstance().databaseHelper.readMessagea();
                        Log.d("records", String.valueOf(records));
                        Log.d("records_length", String.valueOf(String.valueOf(records).length()));

                        for (MessageBean record : records) {
                        MySqlDataBean mySqlDataBean = new MySqlDataBean();
                        mySqlDataBean.setMessageid(record.getMessageid());
                        mySqlDataBean.setUser("1");

//                        String nameTonUtf = null;
//                        try {
//                            nameTonUtf = new String(record.getName().getBytes("utf-8"),"latin1");
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                        mySqlDataBean.setType(nameTonUtf);


                        mySqlDataBean.setType(record.getName());

                        Log.e("getName", "======== " + record.getName());

                        mySqlDataBean.setData(record.getWeight());
                        mySqlDataBean.setPrice(record.getPrice());
                        mySqlDataBean.setTotal(record.getSum());
                        String DataAndTimeStr = DateUtil.getFormattedDateandTime(Long.parseLong(record.getMessageid()));
                        mySqlDataBean.setTime(DataAndTimeStr);
                        mySqlDataBean.setVip(record.getVip());
                        DBUtils.setInfoByMessage(mySqlDataBean);
                        }
//                        DBUtils.setInfoByMessage1();
                    }
                }).start();



            }
        });
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);

        ConnectButton = (Button) findViewById(R.id.Connect_Bt);//获得连接按钮对象
        IPEditText = (EditText) findViewById(R.id.ip_ET);//获得ip文本框对象
        PortText = (EditText) findViewById(R.id.Port_ET);//获得端口文本框按钮对象
        Month_ET = (EditText) findViewById(R.id.month_et);
        Day_ET = (EditText)findViewById(R.id.day_et);
        SendButton = (Button) findViewById(R.id.download_message);//更新信息按钮
        btn_insert_data = (Button) findViewById(R.id.upload);//上传按钮
        progressBar21 = (ProgressBar)findViewById(R.id.progressBar21);//进度条  连接单片机时转圈圈
        progressBar21.setVisibility(-1);//进度条不显示

        setListener();
    }


    /**
     * 连接单片机  连接按钮or断开按钮
     */
    @SuppressLint("WrongConstant")
    public void Connect_onClick(View v) {
        if (isConnect == true) //标志位 = true表示连接
        {
            progressBar21.setVisibility(0);//显示进度条
            tcpClientCountDownTimer.cancel();
            tcpClientCountDownTimer.start();
            ConnectFlage = true;
            ShowPointSum = 0;
            //启动连接线程
            Connect_Thread connect_Thread = new Connect_Thread();
            connect_Thread.start();
        }
        else //标志位 = false表示退出连接
        {
            isConnect = true;//置为true
            ConnectButton.setText("连接");//按钮上显示连接
            try
            {
                socket.close();//关闭连接
                socket=null;
            }
            catch (IOException e)
            {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //单片机数据连接线程<连接完后启动接受线程>
    public class Connect_Thread extends Thread//继承Thread
    {
        @SuppressLint("WrongConstant")
        public void run()//重写run方法
        {
            while(ConnectFlage)
            {
                try
                {
                    if (socket == null)
                    {
                        //用InetAddress方法获取ip地址
                        InetAddress ipAddress = InetAddress.getByName(IPEditText.getText().toString());
                        int port =Integer.valueOf(PortText.getText().toString());//获取端口号
                        socket = new Socket(ipAddress, port);//创建连接地址和端口
                        isConnect = false;
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                tcpClientCountDownTimer.cancel();
                                progressBar21.setVisibility(-1);//关闭滚动条
                                ConnectButton.setText("断开");
                                ConnectFlage = false;

                            }
                        });
                        Log.e("连接成功", "" + ipAddress.toString());
                        //在创建完连接后启动接收线程
                        Receive_Thread receive_Thread = new Receive_Thread();
                        receive_Thread.start();
                    }
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    // 单片机数据接收线程
    private class Receive_Thread extends Thread {
        public void run() {
            byte[] buffer = new byte[1024 * 4];
            int bytes;
            InputStream is = null;
            try {
                is = socket.getInputStream();
                while (true) {
                    if ((bytes = is.read(buffer)) > 0) {
                        byte[] buf_data = new byte[bytes];
                        for (int i = 0; i < bytes; i++) {
                            buf_data[i] = buffer[i];
                        }
//                        String s = new String(buf_data,"GB2312");//得到的数据用GB2312，正常显示汉字
                        String s = new String(buf_data,"UTF8");//UTF-8，调试助手正常显示汉字
                        Log.e("单片机的数据", "" + s.toString());

                        if(s.length() > 10){
                            Message msg = new Message();
                            msg.what = BANNER_VISIBLE;
                            msg.obj = s.toString();
                            mHandler.sendMessage(msg);
                        }
                        else{
                            s = null;
                        }
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //价格更新按钮
    public void Reload_onClick(View v) {
        String name = Month_ET.getText().toString().trim();
        String jiage = Day_ET.getText().toString().trim();
        String nameTonUtf = null;
        try {
            nameTonUtf = new String(name.getBytes("utf-8"),"utf-8");
            Log.d("nameTonUtf", "已转码" + nameTonUtf);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Send_Thread send_Thread = new Send_Thread("update price set danjia = " + jiage + " where name = '" + nameTonUtf +"';" );//更新价格
        Log.d("update", name + "-----" + jiage);
        send_Thread.start();
    }

    /***
     * 将所有价格表直接传递给单片机端，直接插入数据库
     */
    public void update_onClick(View v) throws InterruptedException {
        listprice = GlobalUtil.getInstance().databaseHelper.readPrice();

        for (PriceBean list : listprice){
            String nameTonUtf = null;
            String name = list.getName();
            String jiage = list.getJiage();
            try {
                nameTonUtf = new String(name.getBytes("utf-8"),"utf-8");
                Log.d("nameTonUtf", "已转码" + nameTonUtf);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Send_Thread send_Thread = new Send_Thread("insert into price (name,danjia) values " + "('" +  nameTonUtf + "'," + jiage +  ");"  );//更新数据库最新时间的数据
            send_Thread.start();
            Thread.currentThread().sleep(500);

        }
    }

    //更新数据库中没有的数据，得到数据库中messageid最大的一个，将其发送给单片机，以此更新数据
    public void Send_onClick(View v) {
//        LinkedList<String> DataListRenew = new LinkedList<>();
//        DataListRenew = GlobalUtil.getInstance().databaseHelper.getAvailableDate();
//        String DataRenew = null;
//        String DataRenew1 = null;
//        if (!DataListRenew.contains(DateUtil.getFormattedDate())) {
//            DataListRenew.addLast(DateUtil.getFormattedDate());
//        }
//        if (!CollectionUtils.isEmpty(DataListRenew)) {
//            DataRenew1 = Collections.max(DataListRenew);
//            DataListRenew.remove(DataRenew1);//去除当天
//            if (!CollectionUtils.isEmpty(DataListRenew)){
//                DataRenew = Collections.max(DataListRenew);
//            }
//        }

        String maxid = GlobalUtil.getInstance().databaseHelper.getMaxMessageId();
        Send_Thread send_Thread = new Send_Thread("select * from message where id > " + maxid );//更新数据库最新时间的数据
        //调试
//        Send_Thread send_Thread = new Send_Thread("select * from message ");
        send_Thread.start();
    }

    // 单片机发送线程
    class Send_Thread extends Thread
    {
        String msg;
        public Send_Thread(String msg){
            this.msg=msg;
        }
        public void run()
        {
            try {
                outputStream = socket.getOutputStream();
                outputStream.write(msg.getBytes());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //测试按钮                              订单，名字，单价，重量，总价，vip
    public void Test_onClick(View v){
        String str =new String("1556683932,苹果,5,2.3,4.61,00;" +
                                        "1556683932,梨子,2.5,2.5,4.82,00;" +
                                        "1556683932,香蕉,4,4.2,4.22,00;" +

                                        "1556644332,白菜,1.5,2.8,4.22,1440830301;" +
                                        "1556644332,茄子,3.5,1.6,4.22,1440830301;" +
                                        "1556644332,胡萝卜,1.8,6.3,4.22,1440830301;" +
                                        "1556644332,辣椒,3,0.7,4.22,1440830301;" +

                                        "1556758905,苹果,5,4.2,4.61,1440830302;" +
                                        "1556758905,草莓,1,5.3,4.61,1440830302;" +
                                        "1556758905,西瓜,1.2,12.6,4.61,1440830302;" +
                                        "1556758905,香蕉,4,2.3,4.61,1440830302;" +
                                        "1556758905,葡萄,2,3.3,4.61,1440830302;" +

                                        "1557129743,玉米,2,4.5,4.82,1440830303;" +
                                        "1557129743,大葱,0.6,0.7,4.22,1440830303;" +

                                        "1557382054,土豆,1.5,12,4.61,00;" +
                                        "1557382054,黄瓜,1.4,15,4.61,00;" +
                                        "1557382054,洋葱,2,9.8,4.61,00;" +
                                        "1557382054,莲花白菜,1.5,13,4.61,00;" +

                                        "1560564154,菠萝,2,3.3,4.82,1440830304;" +

                                        "1564232996,蘑菇,7,3.3,4.61,00;" +
                                        "1564232996,香蕉,4,3.1,4.61,00;" +

                                        "1574478133,西瓜,1.2,12.6,4.61,1440830302;" +
                                        "1574478133,香蕉,4,2.3,4.61,1440830302;" +
                                        "1574478133,葡萄,2,3.3,4.61,1440830302;" +

                                        "1575167663,土豆,1.5,12,4.61,14408303013;" +
                                        "1575167663,黄瓜,1.4,15,4.61,14408303013;" +
                                        "1575167663,洋葱,2,9.8,4.61,14408303013;" +

                                        "1575357165,苹果,5,4.2,4.61,1440830312;" +
                                        "1575357165,草莓,1,5.3,4.61,1440830312;" +
                                        "1575357165,西瓜,1.2,12.6,4.61,1440830312;" +
                                        "1575357165,香蕉,4,2.3,4.61,1440830312;" +
                                        "1575357165,葡萄,2,3.3,4.61,1440830312;"
        );

        Log.e("测试的数据", "" + str.toString());

        Message msg = new Message();
        msg.what = BANNER_VISIBLE;
        msg.obj = str.toString();
        mHandler.sendMessage(msg);
        Toast.makeText(getApplicationContext(), "添加成功",Toast.LENGTH_SHORT).show();

    }

    /***
     * 延时3s的定时器  显示连接的小数点个数
     */
    private CountDownTimer tcpClientCountDownTimer = new CountDownTimer(3000,200) {
        @Override
        public void onTick(long millisUntilFinished) {//每隔200ms进入
            if (ConnectFlage)
            {	ShowPointSum ++;
                switch (ShowPointSum%9)
                {   case 0:ConnectButton.setText("连接中");break;
                    case 1:ConnectButton.setText("连接中.");break;
                    case 2:ConnectButton.setText("连接中..");break;
                    case 3:ConnectButton.setText("连接中...");break;
                    case 4:ConnectButton.setText("连接中....");break;
                    case 5:ConnectButton.setText("连接中.....");break;
                    case 6:ConnectButton.setText("连接中......");break;
                    case 7:ConnectButton.setText("连接中.......");break;
                    case 8:ConnectButton.setText("连接中........");break;
                    default:
                        break;
                }
            }
        }
        @SuppressLint("WrongConstant")
        @Override
        public void onFinish() {//3s后进入(没有取消定时器的情况下)
            if (ConnectFlage)
            {	ConnectFlage = false;
                progressBar21.setVisibility(-1);//关闭滚动条
                ConnectButton.setText("重新连接");
                Toast.makeText(getApplicationContext(),"连接失败",Toast.LENGTH_SHORT).show();
            }
            tcpClientCountDownTimer.cancel();
        }
    };

    /***
     * 手机返回按钮  关闭所有线程，返回主界面
     */
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 判断间隔时间 大于2秒就退出应用
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                Toast.makeText(getApplicationContext(), "再按一次退出下载页",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else
            {
                ConnectFlage = false;//取消连接任务
                tcpClientCountDownTimer.cancel();//结束定时器
                try
                {
                    if (socket!=null)
                    {
                        socket.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                try
                {
                    if (outputStream!=null)
                    {
                        outputStream.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                try
                {
                    if (inputStream!=null)
                    {
                        inputStream.close();
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                DownLoad.this.finish();
            }
            return false;
        }
        return false;
    }

}