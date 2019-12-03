package com.cainiaoalliance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cainiaoalliance.Mysql.DBUtils;
import com.cainiaoalliance.Mysql.MySqlDataBean;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class PriceListview extends AppCompatActivity {

    private EditText et_name;
    private EditText et_price;
    private ImageButton bt_add;
    private ImageButton bt_edit;
    private String name;
    private String price;

    private PriceBean priceBean1;
    private PriceBean priceBean2;
    private PriceBean priceBean3;
    private LinkedList<PriceBean> list = new LinkedList<>();
    private MyAdapter adapter;
    private ListView listView1;
    private PopupWindow pw;
    private TextView delete;

//    Socket socket = null;//定义socket
//    private OutputStream outputStream=null;//定义输出流

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_listview);
        initView();
        initDate();
        setListener();
        // 控件的初始化
//        btn_get_data = findViewById(R.id.btn_get_data);
//        tv_data = findViewById(R.id.tv_data);
//        setListener1();
    }

    //此方法的作用是创建一个选项菜单，我们要重写这个方法
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.price_title_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //在点击这个菜单的时候，会做对应的事，类似于侦听事件，这里我们也要重写它
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //这里是一个switch语句,主要通过menu文件中的id值来确定点了哪个菜单，然后做对应的操作，这里的menu是指你加载的那个菜单文件
        switch (item.getItemId()) {
            case R.id.button_reload:
                Intent intent = new Intent(PriceListview.this, DownLoad.class);
                startActivity(intent);
                Toast.makeText(this, "跳转到下载页", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_connect:

                Toast.makeText(this, "重置价格信息 TO 电子秤", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


//    //单片机数据连接线程<连接完后启动接受线程>
//    public class Connect_Thread extends Thread//继承Thread
//    {
//        @SuppressLint("WrongConstant")
//        public void run()//重写run方法
//        {
//            try
//            {
//                if (socket == null)
//                {
//                    //用InetAddress方法获取ip地址
//                    InetAddress ipAddress = InetAddress.getByName("192.168.4.1");
//                    int port =Integer.valueOf("8080");//获取端口号
//                    socket = new Socket(ipAddress, port);//创建连接地址和端口
//
//                    Log.e("连接成功", "" + ipAddress.toString());
//                    //在创建完连接后启动发送线程
//
//
//
//
//                    Send_Thread send_Thread = new Send_Thread("insert " + "");//更新数据库最新时间的数据
//                    send_Thread.start();
//                }
//            }
//            catch (Exception e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    // 发送单片机线程
//    public class Send_Thread extends Thread
//    {
//        String msg;
//        public Send_Thread(String msg){
//            this.msg=msg;
//        }
//        public void run()
//        {
//            try {
//                outputStream = socket.getOutputStream();
//                outputStream.write(msg.getBytes());
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }






    //初始化视图
    private void initView() {
        et_name=(EditText) findViewById(R.id.et_name);
        et_price=(EditText) findViewById(R.id.et_price);
        bt_add=(ImageButton) findViewById(R.id.bt_add);
        bt_edit=(ImageButton) findViewById(R.id.bt_edit);
        listView1=(ListView) findViewById(R.id.listView1);
    }

    /**
     * 设置监听  修改数据
     */
    private void setListener() {
        // 按钮点击事件
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString().trim();
                price = et_price.getText().toString().trim();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(price)){
                    Toast.makeText(PriceListview.this, "修改信息不能为空", Toast.LENGTH_LONG).show();

                }else{
                    priceBean3 = new PriceBean(name);
                    PriceBean findName = GlobalUtil.getInstance().databaseHelper.findNameFromPrice(priceBean3);
                    if(name.equals(findName.getName())){
                        boolean edit = GlobalUtil.getInstance().databaseHelper.editPriceRecord(name,price);
                        if(edit){
                            list = GlobalUtil.getInstance().databaseHelper.readPrice();
                            adapter.notifyDataSetInvalidated();
                            Toast.makeText(PriceListview.this, "修改成功" + name + "---" + price, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(PriceListview.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(PriceListview.this, "未找到此物品！\n ----****请先添加****----", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }


    //初始化数据
    private void initDate() {
        list = GlobalUtil.getInstance().databaseHelper.readPrice();
        adapter=new MyAdapter();
        listView1.setAdapter(adapter);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = et_name.getText().toString().trim();
                price = et_price.getText().toString().trim();
                priceBean1 = new PriceBean(name,price);

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(price)){
                    Toast.makeText(PriceListview.this, "添加信息不能为空", Toast.LENGTH_LONG).show();

                }else{
                    priceBean2 = new PriceBean(name);
                    PriceBean findName = GlobalUtil.getInstance().databaseHelper.findNameFromPrice(priceBean2);
                    if(name.equals(findName.getName())){
                        Toast.makeText(PriceListview.this, "添加的品名不能一样！", Toast.LENGTH_SHORT).show();

                    }else{
                        boolean add = GlobalUtil.getInstance().databaseHelper.addPrice(priceBean1);
                        if(add){
                            list = GlobalUtil.getInstance().databaseHelper.readPrice();
                            adapter.notifyDataSetInvalidated();
                            Toast.makeText(PriceListview.this, "添加成功" + name + "---" + price, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(PriceListview.this, "添加失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });


        /**
         * listview的条目点击事件
         */
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private String na;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                na = list.get(position).getName();
                View v = View.inflate(PriceListview.this, R.layout.price_popu_window, null);
                if (pw != null) {
                    pw.dismiss();//让弹出的PopupWindow消失
                    pw = null;
                }
                pw = new PopupWindow(v, -2, -2);
                int [] location=new int[2];
                view.getLocationInWindow(location);
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pw.showAtLocation(parent, Gravity.RIGHT+ Gravity.TOP, 20,location[1]-5 );//设置显示的位置
                ScaleAnimation animation = new ScaleAnimation(0.3f, 1f, 0.3f, 1f, Animation.RELATIVE_TO_SELF,
                        Animation.RELATIVE_TO_SELF);//弹出的动画
                animation.setDuration(400);//设置动画时间
                v.startAnimation(animation);//开启动画
                delete = (TextView)v.findViewById(R.id.tv_delete);

                /**
                 * 删除每一个item上的数据
                 */
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GlobalUtil.getInstance().databaseHelper.removePriceRecord(na);
                        list.remove(position);//移除item的条目
                        list = GlobalUtil.getInstance().databaseHelper.readPrice();//调用查询所有重新再查找一遍
                        adapter.notifyDataSetChanged();//更新适配器
                    }
                });
            }
        });


        /**
         * listview的滑动监听
         * 当手上下滑动的时候让PopupWindow消失
         */
        listView1.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(pw!=null){
                    pw.dismiss();
                    pw=null;
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private String name2;
        private String price2;
        private View view;

        @SuppressLint("ViewHolder") @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder=null ;//设置静态类使其初始化
            if(convertView==null){

                holder = new ViewHolder();//创建holder对象
                view = View.inflate(PriceListview.this, R.layout.price_list_view,null );
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder.tv_sex = (TextView) view.findViewById(R.id.tv_price);

                view.setTag(holder);//用来保存一些数据结构。
            }else{
                view=convertView;//复用历史缓存
                holder=(ViewHolder) view.getTag();

            }
            name2 = list.get(position).getName();
            price2 = list.get(position).getJiage();

            holder.tv_name.setText(name2);
            holder.tv_sex.setText(price2);
            return view;
        }

        @Override
        public int getCount() {
            return list.size();    //返回list集合中的数据个数
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    //ViewHolder静态类
    static class ViewHolder{
        TextView tv_name;
        TextView tv_sex;
    }
}









//    private Button btn_get_data;
//    private TextView tv_data;
//
//    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//
//            switch (msg.what){
//                case 0x11:
//                    String s = (String) msg.obj;
//                    tv_data.setText(s);
//                    break;
//                case 0x12:
//                    String ss = (String) msg.obj;
//                    tv_data.setText(ss);
//                    break;
//            }
//
//        }
//    };
//    /**
//     * 设置监听
//     */
//    private void setListener1() {
//
//        // 按钮点击事件
//        btn_get_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // 创建一个线程来连接数据库并获取数据库中对应表的数据
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
//                        HashMap<String, Object> map = DBUtils.getInfoByName("9221123");
//                        Message message = handler.obtainMessage();
//                        if(map != null){
//
//                            String s = "";
//                            String aa = "";
//
//                            for (String key : map.keySet()){
//
//                                s += key + ":" + map.get(key) + "\n";
//
//                            }
////                            String aa = new String(s.getBytes("utf8"),"UTF8");//UTF-8，调试助手正常显示汉字
//                            message.what = 0x12;
//                            message.obj = s;
//                        }else {
//                            message.what = 0x11;
//                            message.obj = "查询结果为空";
//                        }
//                        // 发消息通知主线程更新UI
//                        handler.sendMessage(message);
//                    }
//                }).start();
//
//            }
//        });
//
//    }