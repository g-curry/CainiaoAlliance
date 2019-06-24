package com.cainiaoalliance;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;
import java.util.List;

public class DownLoad extends AppCompatActivity {

    boolean isConnect=true;//连接还是断开
    Button ConnectButton;//定义连接按钮
    Button SendButton;//定义发送按钮
    EditText IPEditText;//定义ip输入框
    EditText PortText;//定义端口输入框
    //    EditText Send_ET;//定义信息输出框
    EditText Month_ET;//定义更新数据的月份
    EditText Day_ET;//定义更新数据的日期
    EditText RrceiveEditText;//定义信息输入框
    Socket socket = null;//定义socket
    private OutputStream outputStream=null;//定义输出流
    private InputStream inputStream=null;//定义输入流


//    private Socket serciceClient = null;
//    private ServerThread mServerThread;

    long exitTime=0;//返回按键计时

    private String data=new String();
    private Context context;
    private static int BANNER_VISIBLE = 1;

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

            for(int i=0; i < lie.length; i++){
                String messageidStr =lie[i][0];
                String priceStr =lie[i][2];
                String weightStr =lie[i][3];
                String sumStr =lie[i][4];
                String dataStr = DateUtil.getFormattedDateA(Long.parseLong(messageidStr));
                String timeStr = DateUtil.getFormattedTime(Long.parseLong(messageidStr));

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
            }
        }
    };

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

//        RrceiveEditText = (EditText) findViewById(R.id.Receive_ET);//获得接收消息文本框对象
    }

    public void Connect_onClick(View v) {
        if (isConnect == true) //标志位 = true表示连接
        {
            isConnect = false;//置为false
            ConnectButton.setText("断开");//按钮上显示--断开
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



//    private class ServerThread extends Thread {
//        // 连接服务器
//        public void run() {
//            try {
//
//                serciceClient = new Socket("192.168.1.100", 8086);
//                serciceClient.setSoTimeout(30 * 1000);
//
//            } catch (IOException e) {
//
//                e.printStackTrace();
//
//
//            }
//        }
//    }
//
//
//    // 发送数据
//    private void sendMessageHandle(MessageBean interaction) {
//        if (serciceClient == null) {
//            Toast.makeText(this, "没有服务器连接", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        try {
//            OutputStream os = serciceClient.getOutputStream();
//            os.write(GsonTools.createGsonString(interaction).getBytes());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    public void Upload_onClick(View v){

    }
    public void Test_onClick(View v){

        String str =new String("1554523375,苹果,2,2.3,4.61,21231312,2019-5-2;1559571576,梨子,2,2.3,4.82,9221123,2019-5-3;1551844975,香蕉,2,2.3,4.22,213213123,2019-6-2");
//        String str =new String("1559577897,苹果,1.2,1.0,1.2,123213123;1559577849,苹果12,1.3,1.0,1.3,123213123;1559577899,苹果2,1.3,1.0,1.3,123213123");

        Log.e("测试的数据", "" + str.toString());

        Message msg = new Message();
        msg.what = BANNER_VISIBLE;
        msg.obj = str.toString();
        mHandler.sendMessage(msg);
        Toast.makeText(getApplicationContext(), "添加成功",Toast.LENGTH_SHORT).show();

    }


    //发送数据给单片机按键处理
    public void Send_onClick(View v) {

//        Send_Thread send_Thread = new Send_Thread("select DISTINCT * from Message where date = " + Month_ET.getText().toString() + "-"
//                + Day_ET.getText().toString() + " order by time asc;");
        //调试
        Send_Thread send_Thread = new Send_Thread("select * from message ");
        send_Thread.start();
    }
    // 发送单片机线程
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

    //连接线程<连接完后启动接受线程>
    class Connect_Thread extends Thread//继承Thread
    {
        public void run()//重写run方法
        {
            try
            {
                if (socket == null)
                {
                    //用InetAddress方法获取ip地址
                    InetAddress ipAddress = InetAddress.getByName(IPEditText.getText().toString());
                    int port =Integer.valueOf(PortText.getText().toString());//获取端口号
                    socket = new Socket(ipAddress, port);//创建连接地址和端口
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

    // 接收线程
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

                        String s = new String(buf_data,"GB2312");//得到的数据用GB2312，正常显示汉字
                        Log.e("单片机的数据", "" + s.toString());

                        Message msg = new Message();
                        msg.what = BANNER_VISIBLE;
                        msg.obj = s.toString();
                        mHandler.sendMessage(msg);
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

    //手机返回按钮
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
