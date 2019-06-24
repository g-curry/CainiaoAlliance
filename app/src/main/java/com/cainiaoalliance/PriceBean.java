package com.cainiaoalliance;

/**
 * @author g_curry   2019/5/27 16:09
 */
public class PriceBean {
    private String name;	    // 品名
    private int price;	    	// 价格
    private String date;    	// 日期
    private long timeStamp;		// 时间戳

    private String uuid;    	// id 引入UUID

    public PriceBean(){
        timeStamp = System.currentTimeMillis();
        date = DateUtil.getFormattedDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
