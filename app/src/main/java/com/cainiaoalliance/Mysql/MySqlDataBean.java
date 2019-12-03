package com.cainiaoalliance.Mysql;

/**
 * Created by g_curry.
 * Date: 2019/9/6 21:45
 * Description:
 */
public class MySqlDataBean {
    //    public MySqlDataBean(int s, int s1, String s3, int s4, int s5, int s6, String s7) {
//    }



    public String messageid;  //订单号
    public String user;	    // 1
    public String type;	    // 名字
    public String data;     	// 重量
    public String price;		    // 单价
    public String total;     	// 总价
    public String vip;
    public String time;	// 时间戳

    public void setUser(String user) {
        this.user = user;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }
}
