package com.jdbc.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 通过获取配置文件，使用jdbc方式获取数据表的元数据
 * @author CourageAQ
 * @date 2018/11/12 16:30
 */
public class SqlUtils {

    private  static final  SqlUtils sqlUtils = new SqlUtils();
    private static final  int batchSize = 10000;
    /**驱动名称*/
    private static String driverClassName = null;
    /**地址链接*/
    private static String url = null;
    /**用户名*/
    private static String username = null;
    /**用户密码*/
    private  static String password = null;
    /**获取属性的值 key-value*/
    private static Properties properties = new Properties();
    /**读取数据库配置文件*/
    private static InputStream in = SqlUtils.class.getResourceAsStream("/database.properties");

    /***
     * 初始化静态变量，从配置文件中读取
     */
    static  {
        try{
            properties.load(in);
            driverClassName = properties.getProperty("jdbc.dbOne.driver").trim();
            url = properties.getProperty("jdbc.dbOne.url").trim();
            username = properties.getProperty("jdbc.dbOne.username").trim();
            password = properties.getProperty("jdbc.dbOne.password").trim();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 构造函数
     */
    public SqlUtils() {}

    public  static  SqlUtils getInstance(){
        return  sqlUtils;
    }

    /**
     * 建立数据库连接
     * @return java.sql.Connection
     */
    private Connection getConnection(){
        try{
            Class.forName(driverClassName);
            Connection connection = DriverManager.getConnection(url,username,password);
            return  connection;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    /**
     *
     * @param sql       sql语句
     * @return          JSONArray
     */
    public JSONArray select(String sql){
        return  select(null,sql,null);
    }

    /**
     *
     * @param sql       sql 语句
     * @param params    sql 参数
     * @return
     */
    public JSONArray select(String sql,List<Object> params){
        return  select(null,sql,params);
    }


    /***
     *
     * @param dbType    多个数据源时，区分
     * @param sql       sql语句
     * @return          JSONArray
     */
    public JSONArray select(String dbType,String sql){
        return  select(null,sql,null);
    }

    /**
     * 查询结果
     * @param dbType
     * @param sql
     * @param params
     * @return  JSONArray
     */
    public  JSONArray select(String dbType,String sql,List<Object> params){
        PreparedStatement ps = null;
        ResultSet rs = null;
        JSONArray result=new JSONArray();
        JSONObject jsonObject;
        Connection conn = null;
        try{
            conn = getConnection();
            ps = (PreparedStatement)conn.prepareStatement(sql);
            if (params != null){
                Object param = null;
                for (int i=0;i<params.size();i++){
                    param = params.get(i);
                    if (param instanceof Integer) {
                        ps.setInt(i + 1, ((Integer) param).intValue());
                    } else if (param instanceof String) {
                        ps.setString(i + 1, (String) param);
                    } else if (param instanceof Double) {
                        ps.setDouble(i + 1, ((Double) param).doubleValue());
                    } else if (param instanceof Float) {
                        ps.setFloat(i + 1, ((Float) param).floatValue());
                    } else if (param instanceof Long) {
                        ps.setLong(i + 1,((Long) param).longValue());
                    } else if (param instanceof Boolean) {
                        ps.setBoolean(i + 1, ((Boolean) param).booleanValue());
                    } else if (param instanceof Date) {
                        ps.setDate(i + 1, (Date) param);
                    } else if (param instanceof BigDecimal) {
                        ps.setBigDecimal(i + 1, (BigDecimal) param);
                    } else if (param instanceof Timestamp) {
                        ps.setTimestamp(i + 1, (Timestamp) param);
                    } else if (param == null){
                        ps.setObject(i + 1, param);
                    }
                }
            }
            rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData();
            List<String> columns = new ArrayList<String>();
            for (int i = 1; i<=md.getColumnCount();i++){
                columns.add(md.getColumnName(i));
            }
           while (rs.next()){
               jsonObject = new JSONObject();
               for(int i = 0; i<columns.size();i++){
                   Object data = rs.getObject(i+1) == null? "" :rs.getObject(i+1);
                   jsonObject.put(columns.get(i),data);
               }
               result.add(jsonObject);
           }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if (rs != null){
                    rs.close();
                }
                if (ps != null){
                    ps.close();
                }
                if (conn != null){
                    conn.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return  result;
    }
}
