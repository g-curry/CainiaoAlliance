package com.cainiaoalliance;



/**
 * @author g_curry   2019/5/27 16:09
 */
public class PriceBean  {
    private String name;	    // 品名
    private String jiage;	    	// 价格
    private String uuid;    	// id 引入UUID，保证唯一性，选取ID进行增删

    public PriceBean(String name, String price) {

        this.name = name;
        this.jiage = price;
    }

    public PriceBean(String name) {

        this.name = name;
    }

    public PriceBean() {

    }

//    public PriceBean{
//
//    uuid = UUID.randomUUID().toString();
//    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJiage() {
        return jiage;
    }

    public void setJiage(String jiage) {
        this.jiage = jiage;
    }

//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }
}
