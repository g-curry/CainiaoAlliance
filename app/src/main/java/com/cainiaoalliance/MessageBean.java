package com.cainiaoalliance;

/**
 * @author g_curry   2019/5/28 19:53
 */
public class MessageBean {

    private String messageid;  //订单号
    private String name;	    // 品名
    private String price;	    // 价格
    private String weight;     	// 重量
    private String sum;		    // 总价
    private String vip;     	// 会员
    private String date;    	// 日期
    private String timeStamp;	// 时间戳

    public MessageBean() {
//        timeStamp = System.currentTimeMillis();
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
