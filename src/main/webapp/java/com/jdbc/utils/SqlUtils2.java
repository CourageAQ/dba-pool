package com.jdbc.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * HikariCP 连接池读取sql元数据
 * @author CourageAQ
 * @date 2018/11/14 15:21
 */
public class SqlUtils2 {
    private  static final  SqlUtils2 sqlUtils = new SqlUtils2();

    /**数据库连接*/
    private static DataSource dataSource;

    /**获取属性的值 key-value*/
    private static Properties properties = new Properties();
    /**读取数据库配置文件*/
    private static InputStream in = SqlUtils.class.getResourceAsStream("/hikaricp.properties");

    /***
     * 初始化静态变量，从配置文件中读取
     */
    static {
        try{
            properties.load(in);
            HikariConfig config = new HikariConfig();
            /**驱动名称*/
            config.setDriverClassName(properties.getProperty("spring.datasource.driverClassName"));
            /**地址链接*/
            config.setJdbcUrl(properties.getProperty("spring.datasource.url"));
            /**用户名*/
            config.setUsername(properties.getProperty("spring.datasource.username"));
            /**用户密码*/
            config.setPassword(properties.getProperty("spring.datasource.password"));
            /**池中最大链接数量*/
            config.setMaximumPoolSize(Integer.valueOf(properties.getProperty("spring.datasource.MaximumPoolSize")));
            /**池中最小空闲链接数量*/
            config.setMinimumIdle(Integer.valueOf(properties.getProperty("spring.datasource.MinimumIdle")));
            /**设置超时时间毫秒*/
            config.setConnectionTimeout(Integer.valueOf(properties.getProperty("spring.datasource.ConnectionTimeout")));
            /**测试语句*/
            config.setConnectionTestQuery(properties.getProperty("spring.datasource.ConnectionTestQuery"));
            try{
                dataSource = new HikariDataSource(config);
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 构造函数
     */
    public SqlUtils2() {}

    public  static  SqlUtils2 getInstance(){
        return  sqlUtils;
    }
    /**
     * 建立数据库连接
     * @return java.sql.Connection
     */
    public  Connection getConnection() throws Exception{
        if (null == dataSource) {
            return dataSource.getConnection();
        }else{
            return dataSource.getConnection();
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

    public  JSONArray select(String dbType, String sql, List<Object> params){
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        Connection conn = null;
        JSONArray result=new JSONArray();
        JSONObject jsonObject;
        try{
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            resultSet = ps.executeQuery();
            ResultSetMetaData md = resultSet.getMetaData();
            List<String> columns = new ArrayList<String>();
            for (int i = 1; i<=md.getColumnCount();i++){
                columns.add(md.getColumnName(i));
            }
            while (resultSet.next()){
                jsonObject = new JSONObject();
                for (int i = 0;i<columns.size();i++){
                    Object data = resultSet.getObject(i+1) == null ? "" : resultSet.getObject(i+1);
                    jsonObject.put(columns.get(i),data);
                }
                result.add(jsonObject);
            }
            resultSet.close();
            ps.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;

    }


}
