import com.alibaba.fastjson.JSONArray;
import com.jdbc.utils.SqlUtils;
import com.jdbc.utils.SqlUtils2;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


/**
 * @author CourageAQ
 * @date 2018/11/12 18:28
 *
 */


public class SqlUtils2Test {

    SqlUtils2 sqlUtils2 = SqlUtils2.getInstance();


    @Test
    public void select(){
        /**不带条件的查询*/
        String sql = "select  zzdrsl,zzdrsj,sfhc,hcdw from jkxxgl where year(zzdrsj) = '2018'";
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        for(int i=0;i<1;i++){
            System.out.println(sqlUtils2.select(sql));
        }
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
