import com.alibaba.fastjson.JSONArray;
import com.jdbc.utils.SqlUtils;
import com.jdbc.utils.SqlUtils2;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;


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
        JSONArray result = sqlUtils2.select("select  zzdrsl,zzdrsj,sfhc,hcdw from jkxxgl where convert(varchar(10),zzdrsj,23) like '2018%'");
        System.out.println(result);

        JSONArray result1 = sqlUtils2.select("select  zzdrsl,zzdrsj,sfhc,hcdw from jkxxgl where convert(varchar(10),zzdrsj,23) like '2018%'");
        System.out.println("*********" + result1);

    }
}
