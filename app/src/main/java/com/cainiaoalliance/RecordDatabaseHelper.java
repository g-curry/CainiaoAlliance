package com.cainiaoalliance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.LinkedList;

/**
 * @author g_curry   2019/5/28 19:57
 */
public class RecordDatabaseHelper extends SQLiteOpenHelper {
//        public static final String SELECT_PRICE_DB = "select DISTINCT * from Record where date = ? order by time asc;";

    private static String TAG = "RecordDatabaseHelper";
    public static final String SELECT_MESSAGE_DB = "select  * from message where date = ?;";
    public static final String SELECT_RECORD_DATE = "select DISTINCT * from message order by date asc;";
    public static final String SELECT_RECORD_ID = "select DISTINCT * from message order by messageid;";

    public static final String DB_NAME = "message";		// 数据库名称CainiaoAlliance
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


    public RecordDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    // 建立数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MESSAGE_DBA);
//        db.execSQL(CREATE_PRICE_DBB);
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

        long rowId = db.insert(DB_NAME, null, values);
        values.clear();
        return rowId;
    }

    // 改表A
    public void editMessage(String messageid, MessageBean record) {
//        removeRecord(messageid);
        record.setMessageid(messageid);
//        addMessage(record);
    }

    // 查表A
    public LinkedList<MessageBean> readMessage(String dataStr) {

        LinkedList<MessageBean> records = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_MESSAGE_DB, new String[]{dataStr});

        if (cursor.moveToFirst()) {
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
        }
        cursor.close();
        return records;
    }

    //读取哪一天有数据，返回数据的天数
    public LinkedList<String> getAvailableDate() {

        LinkedList<String> dates = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_RECORD_DATE, new String[]{});

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                // 同一天有多笔订单，时间只显示一天
                if (!dates.contains(date)) {
                    dates.add(date);
                }
            }
        }
        cursor.close();
        return dates;
    }


    //读取重复的订单号，
    public LinkedList<String> getAvailableMessageId() {

        LinkedList<String> MessId = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_RECORD_ID, new String[]{});

        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                String messageid = cursor.getString(cursor.getColumnIndex("messageid"));

                if (!MessId.contains(messageid)) {
                    MessId.add(messageid);
                }
            }
        }
        cursor.close();
        return MessId;
    }

    //    public static final String CREATE_PRICE_DBB = "create table price ("  //数据表B-价格表详情price
//            + "id integer primary key autoincrement, "
//            + "name text, "
//            + "price integer, "
//            + "time integer, "
//            + "date date, "
//            + "uuid text);";

//    public long addPrice(PriceBean bean) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("name", bean.getName());
//        values.put("price", bean.getPrice());
//        values.put("time", bean.getTimeStamp());
//        values.put("date", bean.getDate());
//        values.put("uuid", bean.getUuid());
//
//        long rowId = db.insert(DB_NAME, null, values);
//        Log.d("RecordDatabaseHelper", bean.getUuid() + "add");
//        values.clear();
//        return rowId;
// 刪
//    public void removeRecord(int messageid) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(DB_NAME, "messageid = ?",messageid);


//    }

//    }

    // 改表B
//    public void editPrice(String uuid, PriceBean record) {
////        removeRecord(uuid);
//        record.setUuid(uuid);
//        addPrice(record);
//    }


//    // 查表B
//    public LinkedList<PriceBean> readPrice(String dataStr) {
//
//        LinkedList<PriceBean> records = new LinkedList<>();
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(SELECT_PRICE_DB, new String[]{dataStr});
//
//        if (cursor.moveToFirst()) {
//            while (cursor.moveToNext()) {
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                int price = cursor.getInt(cursor.getColumnIndex("price"));
//                long timeStamp = cursor.getLong(cursor.getColumnIndex("time"));
//                String date = cursor.getString(cursor.getColumnIndex("date"));
//                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
//
//                PriceBean record = new PriceBean();
//                record.setName(name);
//                record.setPrice(price);
//                record.setTimeStamp(timeStamp);
//                record.setDate(date);
//                record.setUuid(uuid);
//
//                records.add(record);
//            }
//        }
//        cursor.close();
//        return records;
//    }

}
