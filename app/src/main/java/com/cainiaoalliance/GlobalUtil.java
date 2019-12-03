package com.cainiaoalliance;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.cainiaoalliance.RecordDatabaseHelper.DB_NAME;

/**
 * @author g_curry   2019/5/29 18:59
 */
public class GlobalUtil {

    private static GlobalUtil instance; //创建GlobalUtil的一个对象
    public MainActivity mainActivity;

    public RecordDatabaseHelper databaseHelper;
    private Context context;

    public GlobalUtil() {

    }

    //获取唯一可用的对象
    public static GlobalUtil getInstance() {
        if (instance == null) {
            instance = new GlobalUtil();
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        System.out.println(String.format("name = %d, sex =%d", 1, 1));

        this.context = context;
        databaseHelper = new RecordDatabaseHelper(context, DB_NAME, null, 1);


//        SQLiteDatabase dbR = databaseHelper.getReadableDatabase();
//        Cursor c = dbR.query("message", null, null, null, null, null, null);

//        while (c.moveToNext()) {
//            String name = c.getString(c.getColumnIndex("name"));
//            System.out.println(String.format("name111111111111111111111 = %s", name));
//        }

    }
}
