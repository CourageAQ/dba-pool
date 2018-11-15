import com.alibaba.fastjson.JSONArray;
import com.jdbc.utils.SqlUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;


/**
 * @author CourageAQ
 * @date 2018/11/12 18:28
 */


public class SqlUtilsTest {

    SqlUtils sqlUtils = SqlUtils.getInstance();


    @Test
    public void select(){
        /**不带条件的查询*/

        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        JSONArray result = sqlUtils.select("select  zzdrsl,zzdrsj,sfhc,hcdw from jkxxgl where convert(varchar(10),zzdrsj,23) like '2018%'");
        System.out.println(result);
        /**条件查询
        JSONArray result1 = sqlUtils.select("select * from test_name where daystime = ?", Arrays.asList(new String[]{"2015-12-02"}));
        System.out.println(result1);*/
    }
}
