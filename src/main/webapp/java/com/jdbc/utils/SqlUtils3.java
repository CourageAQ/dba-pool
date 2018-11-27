package com.jdbc.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 阿里巴巴 druid 数据库连接池
 * @author CourageAQ
 * @date 2018/11/27 9:17
 */
public class SqlUtils3 {

    private static SqlUtils3 sqlUtils3 = new SqlUtils3();
    private static Log log = LogFactory.getLog(SqlUtils3.class);
    private static DruidDataSource dataSource = null;
    private static DruidPooledConnection connection = null;
    private static PreparedStatement  ps = null;
    private static ResultSet resultSet = null;

    /**获取属性的值 key-value*/
    private static Properties properties = new Properties();
    /**读取数据库配置文件*/
    private static InputStream in = SqlUtils.class.getResourceAsStream("/druid.properties");

    /**初始化静态代码块，
     * 之后执行initDataSource静态方法，
     * 初始化阿里巴巴的DruidDataSource*/
    static {
        initDataSource();
    }
    /**
     * 初始化阿里的DruidDataSource
     */
    private static void initDataSource(){
        try{
            properties.load(in);
            dataSource = new DruidDataSource();
            dataSource.setUrl(properties.getProperty("spring.datasource.url"));
            dataSource.setUsername(properties.getProperty("spring.datasource.username"));
            dataSource.setPassword(properties.getProperty("spring.datasource.password"));
            dataSource.setDriverClassName(properties.getProperty("spring.datasource.driverClassName"));
            dataSource.setInitialSize(Integer.valueOf(properties.getProperty("spring.datasource.initialSize")));
            dataSource.setMinIdle(Integer.valueOf(properties.getProperty("spring.datasource.minIdle")));
            dataSource.setMaxActive(Integer.valueOf(properties.getProperty("spring.datasource.maxActive")));
            dataSource.setMaxWait(Long.valueOf(properties.getProperty("spring.datasource.maxWait")));
            dataSource.setTimeBetweenEvictionRunsMillis(Long.valueOf(properties.getProperty("spring.datasource.timeBetweenEvictionRunsMillis")));
            dataSource.setMinEvictableIdleTimeMillis(Long.valueOf(properties.getProperty("spring.datasource.minEvictableIdleTimeMillis")));
            dataSource.setPoolPreparedStatements(Boolean.valueOf(properties.getProperty("spring.datasource.poolPreparedStatements")));
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.valueOf(properties.getProperty("spring.datasource.maxPoolPreparedStatementPerConnectionSize")));
            dataSource.setValidationQuery(properties.getProperty("spring.datasource.validationQuery"));
        }catch (Exception e){
            e.printStackTrace();
            log.error("初始化dataSource失败！" + e.getMessage());
        }
    }

    /**无参构造函数*/
    public SqlUtils3() {}

    public  static  SqlUtils3 getInstance(){
        return  sqlUtils3;
    }
    /**
     * Druid 连接
     * @return
     */
    public  DruidPooledConnection getConnection() throws  Exception{
        if (null == dataSource){
            initDataSource();
            return  dataSource.getConnection();
        }else{
            return  dataSource.getConnection();
        }
    }

    /**
     *
     * @param sql       sql语句
     * @return          JSONArray
     */
    public JSONArray search(String sql){
        return  search(null,sql,null);
    }

    public JSONArray search(String dbType,String sql) {
        return search(dbType,sql,null);
    }

    public JSONArray search(String sql,List params) {
        return search(null,sql,params);
    }

    public synchronized   JSONArray search(String dbType, String sql, List params){
        JSONArray result=new JSONArray();
        JSONObject jsonObject;
        try{
            connection = getConnection();
            ps = connection.prepareStatement(sql);
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
            closeAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;

    }

    /***
     * 关闭连接等
     */
    public  void  closeAll(){
        try{
            if (connection != null)connection.close();
            if (ps != null) ps.close();
            if (resultSet != null) resultSet.close();
        }catch (Exception e){
            log.error("关闭连接失败！" + e.getMessage());
        }
    }
}
