package com.cainiaoalliance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Collections;
import java.util.LinkedList;

/**
 * @author g_curry   2019/5/28 19:57
 */
public class RecordDatabaseHelper extends SQLiteOpenHelper {
//        public static final String SELECT_PRICE_DB = "select DISTINCT * from Record where date = ? order by time asc;";

    private static String TAG = "RecordDatabaseHelper";
    public static final String SELECT_MESSAGE_DB = "select  * from message where date = ?;";
    public static final String SELECT_MESSAGE = "select  * from message;";
    public static final String SELECT_PRICE= "select  * from price;";
    public static final String SELECT_RECORD_DATE = "select DISTINCT * from message order by date asc;";
    public static final String SELECT_RECORD_ID = "select DISTINCT * from message order by messageid asc;";
    public static final String DB_NAME = "CainiaoAlliance";		// 数据库名称CainiaoAlliance
    public static final String TABLE_A_NAME = "message";		// 数据表A-订单详情message
    public static final String TABLE_B_NAME = "price";		// 数据表B-价格表详情price
    public static final String CREATE_MESSAGE_DBA = "create table message ("  //数据表A-订单详情message
            + "id integer primary key autoincrement, "
            + "messageid text, "
            + "name text, "
            + "price text, "
            + "weight text, "
            + "sum text, "
            + "vip text, "
            + "time text, "
            + "date date);";

        public static final String CREATE_PRICE_DBB = "create table price ("  //数据表B-价格表详情price
            + "id integer primary key autoincrement, "
            + "name text, "
            + "jiage text);";


    public RecordDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    // 建立数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MESSAGE_DBA);
        db.execSQL(CREATE_PRICE_DBB);
    }

    // 更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    // 增数据表A-订单详情message
    public long addMessage(MessageBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("messageid", bean.getMessageid());
        values.put("name", bean.getName());
        values.put("price", bean.getPrice());
        values.put("weight", bean.getWeight());
        values.put("sum", bean.getSum());
        values.put("vip", bean.getVip());
        values.put("time", bean.getTimeStamp());
        values.put("date", bean.getDate());

        long rowId = db.insert(TABLE_A_NAME, null, values);
        values.clear();
        return rowId;
    }

    // 刪
    public void removeRecord(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_A_NAME, "name = ?", new String[]{name});
    }

    // 查表A  查询表中所有内容，传递给MySql使用
    public LinkedList<MessageBean> readMessagea() {

        LinkedList<MessageBean> records = new LinkedList<>();
        SQLiteDatabase dbR = this.getReadableDatabase();
//        Cursor cursor = dbR.query("message", null, null, null, null, null, null);
        Cursor cursor = dbR.rawQuery(SELECT_MESSAGE, null);


            while (cursor.moveToNext()) {
                String messageid = cursor.getString(cursor.getColumnIndex("messageid"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                String weight = cursor.getString(cursor.getColumnIndex("weight"));
                String sum = cursor.getString(cursor.getColumnIndex("sum"));
                String vip = cursor.getString(cursor.getColumnIndex("vip"));
                String timeStamp = cursor.getString(cursor.getColumnIndex("time"));
                String date = cursor.getString(cursor.getColumnIndex("date"));

                MessageBean record = new MessageBean();
                record.setMessageid(messageid);
                record.setName(name);
                record.setPrice(price);
                record.setWeight(weight);
                record.setSum(sum);
                record.setVip(vip);
                record.setTimeStamp(timeStamp);
                record.setDate(date);

                records.add(record);
            }

        cursor.close();
        return records;
    }


    // 查表A  通过读取当天日期的方式  数据供页面ListView使用
    public LinkedList<MessageBean> readMessage(String dataStr) {

        LinkedList<MessageBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_MESSAGE_DB, new String[]{dataStr});
        while (cursor.moveToNext()) {
            String messageid = cursor.getString(cursor.getColumnIndex("messageid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String price = cursor.getString(cursor.getColumnIndex("price"));
            String weight = cursor.getString(cursor.getColumnIndex("weight"));
            String sum = cursor.getString(cursor.getColumnIndex("sum"));
            String vip = cursor.getString(cursor.getColumnIndex("vip"));
            String timeStamp = cursor.getString(cursor.getColumnIndex("time"));
            String date = cursor.getString(cursor.getColumnIndex("date"));

            MessageBean record = new MessageBean();
            record.setMessageid(messageid);
            record.setName(name);
            record.setPrice(price);
            record.setWeight(weight);
            record.setSum(sum);
            record.setVip(vip);
            record.setTimeStamp(timeStamp);
            record.setDate(date);

            records.add(record);
        }
        cursor.close();
        return records;
    }


    //读取哪一天有数据，返回数据的天数
    public LinkedList<String> getAvailableDate() {

        LinkedList<String> dates = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_RECORD_DATE, new String[]{});

        if (cursor.moveToFirst()){
            do{
                String date = cursor.getString(cursor.getColumnIndex("date"));
                if (!dates.contains(date)){
                    dates.add(date);
                }
            }while (cursor.moveToNext());
        }

        cursor.close();
        return dates;
    }


    //读取哪一天有数据，返回数据的天数
    public String getMaxMessageId() {
        String maxMessageId;
        LinkedList<String> messageId = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_RECORD_ID, new String[]{});

        if (cursor.moveToFirst()){
            do{
                String messageid = cursor.getString(cursor.getColumnIndex("messageid"));
                if (!messageId.contains(messageid)){
                    messageId.add(messageid);
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        maxMessageId = Collections.max(messageId);
        Log.d("maxMessageId", "====getMaxMessageId:   " + maxMessageId);
        return maxMessageId;
    }




    //表B  增加价格
    public boolean addPrice(PriceBean bean) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", bean.getName());
        values.put("jiage", bean.getJiage());

        long insert = db.insert(TABLE_B_NAME, null, values);
        Log.d("Add Price", bean.getName() + "    " + bean.getJiage());
        values.clear();
        db.close();
        if(insert !=-1){
            return true;
        }else{
            return false;
        }
    }


    // 刪
    public void removePriceRecord(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_B_NAME, "name = ?", new String[]{name});
    }

    // 改
    public boolean editPriceRecord(String name, String jiage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("jiage", jiage);
        long edit = db.update(TABLE_B_NAME, values, "name=?", new String[]{name});
        values.clear();
        db.close();
        if(edit !=-1){
            return true;
        }else{
            return false;
        }
    }

    /**
     *通过名字查询价格表
     * @param priceBean
     * @return
     */
    public PriceBean findNameFromPrice(PriceBean priceBean ){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from price where name=?", new String[]{priceBean.getName()});
        PriceBean s = new PriceBean();
        while(cursor.moveToNext()){
//            String name = cursor.getString(1);
            s.setName(priceBean.getName());
        }
        cursor.close();
        db.close();
        return s;
    }

    /**
     *查询价格表的所有内容 用于listview展示
     * @return
     */
    public LinkedList<PriceBean> readPrice() {

        LinkedList<PriceBean> records_price = new LinkedList<>();
        SQLiteDatabase dbR = this.getReadableDatabase();
        Cursor cursor = dbR.rawQuery(SELECT_PRICE, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String jiage = cursor.getString(cursor.getColumnIndex("jiage"));
//                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                PriceBean record = new PriceBean();
                record.setName(name);
                record.setJiage(jiage);
//                record.setUuid(uuid);
                records_price.add(record);
            }
        cursor.close();
        return records_price;
    }

}
