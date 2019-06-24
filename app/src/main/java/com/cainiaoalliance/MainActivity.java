package com.cainiaoalliance;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private MainViewPagerAdapter PagerAdapter;      // 填充ViewPager
    private Context context = MainActivity.this;	// 上下文对象
    private TickerView amountText;					// 总金额
    private TextView dateText;						// 日期
    private int currentPagerPosition = 0;

    long exitTime=0;//返回按键计时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化数据库
        GlobalUtil.getInstance().setContext(getApplicationContext());
        GlobalUtil.getInstance().mainActivity = this;

        handleView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        PagerAdapter.reload();
        updateHeader();
    }

    //此方法的作用是创建一个选项菜单，我们要重写这个方法
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.title_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //在点击这个菜单的时候，会做对应的事，类似于侦听事件，这里我们也要重写它
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //这里是一个switch语句,主要通过menu文件中的id值来确定点了哪个菜单，然后做对应的操作，这里的menu是指你加载的那个菜单文件
        switch (item.getItemId()) {
            case R.id.button_calendar:
                Toast.makeText(this, "选择日期", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_reload:
                Toast.makeText(this, "更新数据", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void handleView() {
        getSupportActionBar().setElevation(0);		// 去除ActionBar的阴影

        amountText = findViewById(R.id.amount_text);
        amountText.setCharacterLists(TickerUtils.provideNumberList());		// 设置amountText使用数字切换效果
        dateText = findViewById(R.id.date_text);

        mViewPager = findViewById(R.id.view_pager);
        PagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        PagerAdapter.notifyDataSetChanged();		// 根据PagerAdapter的更改自动更新
        mViewPager.setAdapter(PagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(PagerAdapter.getLastIndex());		// 初始显示最后面的Fragment

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到下载界面
                Intent intent = new Intent(MainActivity.this, DownLoad.class);
                startActivityForResult(intent, 1);		// 设置请求标记，返回界面刷新listview
                //bug，当没有数据的时候需要退出软件才能更新，有一个数据时退出可以正常刷新
//                startActivity(intent);
            }
        });
    }

    // 更新总金额
    public void updateHeader() {
        String amount = String.valueOf(PagerAdapter.getTotalCost(currentPagerPosition));
        amountText.setText(amount);
        String date = PagerAdapter.getDateStr(currentPagerPosition);
        dateText.setText(DateUtil.getWeekDay(date));
    }

    /***
     * 手机返回按钮
     */
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            // 判断间隔时间 大于2秒就退出应用
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            else
            {
                MainActivity.this.finish();
            }
            return false;
        }
        return false;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        currentPagerPosition = i;
        updateHeader();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
