package com.cainiaoalliance.Mysql;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.cainiaoalliance.MessageBean;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * MySql数据库工具类：连接数据库用、获取数据库数据、增加数据库数据
 *
 */
public class DBUtils {

    private static String driver = "com.mysql.jdbc.Driver";// MySql驱动
    private static String user = "cainiao";// 用户名
    private static String password = "1234";// 密码
    private static Connection getConn(String dbName){

        Connection connection = null;
        try{
            Class.forName(driver);// 动态加载类
            String ip = "47.100.240.98";// 数据库ip地址

            // 尝试建立到给定数据库URL的连接
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + dbName,
                    user, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }



    /**
     * MySql插入数据
     */
    public static void setInfoByMessage(MySqlDataBean mySqlDataBean) {
        // 根据数据库名称，建立连接
        Connection connection = getConn("MessageBean");
        try {
//            String sql = "select * from message where id = ?";
            String sql = "INSERT INTO `message`(`messageid`, `user`, `type`, `data`, `price`, `total`, `time`, `vip`) VALUES (?,?,?,?,?,?,?,?)";  // 插入数据的sql语句
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){

                    ps.setString(1,mySqlDataBean.messageid);
                    ps.setString(2,mySqlDataBean.user);
                    String nameTonUtf = null;
                    try {
                        nameTonUtf = new String(mySqlDataBean.type.getBytes("utf-8"),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


//                    String typeTonUtf = new String(mySqlDataBean.type.getBytes("latin1"),"latin1");
//                    ps.setString(3,typeTonUtf);
                    ps.setString(3,nameTonUtf);

                    ps.setString(4,mySqlDataBean.data);
                    ps.setString(5,mySqlDataBean.price);
                    ps.setString(6,mySqlDataBean.total);
                    ps.setString(7,mySqlDataBean.time);
                    ps.setString(8,mySqlDataBean.vip);

                    ps.executeUpdate();
                    connection.close();
                    ps.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());

        }
    }

    /**
     * MySql插入数据
     */
    public static void setInfoByMessage1() {
        // 根据数据库名称，建立连接
        Connection connection = getConn("MessageBean");
        try {
//            String sql = "select * from message where id = ?";
            String sql = "INSERT INTO `testcn`(`test`) VALUES (?)";  // 插入数据的sql语句
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){

                    String test = new String("苹果");
                    String nameTonUtf = new String(test.getBytes("utf-8"),"utf-8");

                    ps.setString(1,nameTonUtf);

                    ps.executeUpdate();
                    connection.close();
                    ps.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());

        }
    }


    public static HashMap<String, Object> getInfoByName(String name){

        HashMap<String, Object> map = new HashMap<>();
        // 根据数据库名称，建立连接
        Connection connection = getConn("MessageBean");

        try {
            // mysql简单的查询语句。这里是根据MD_CHARGER表的NAME字段来查询某条记录
            String sql = "select * from message where vip = ?";
//            String sql = "select * from MD_CHARGER";
            if (connection != null){// connection不为null表示与数据库建立了连接
                PreparedStatement ps = connection.prepareStatement(sql);
                if (ps != null){
                    // 设置上面的sql语句中的？的值为name
                    ps.setString(1, name);
                    // 执行sql查询语句并返回结果集
                    ResultSet rs = ps.executeQuery();
                    if (rs != null){
                        int count = rs.getMetaData().getColumnCount();
                        Log.e("DBUtils","列总数：" + count);
                        while (rs.next()){
                            // 注意：下标是从1开始的
                            for (int i = 1;i <= count;i++){
                                String field = rs.getMetaData().getColumnName(i);
                                map.put(field, rs.getString(field));
                            }
                        }
                        connection.close();
                        ps.close();
                        return  map;
                    }else {
                        return null;
                    }
                }else {
                    return  null;
                }
            }else {
                return  null;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DBUtils","异常：" + e.getMessage());
            return null;
        }
    }


}

