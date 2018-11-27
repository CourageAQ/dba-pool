import com.jdbc.utils.SqlUtils3;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author CourageAQ
 * @date 2018/11/27 10:25
 */
public class SqlUtils3Test {

    SqlUtils3 sqlUtils3 = SqlUtils3.getInstance();


    @Test
    public void select(){
        /**不带条件的查询*/
        String sql = "select  zzdrsl,zzdrsj,sfhc,hcdw from jkxxgl where convert(varchar(10),zzdrsj,23) like '2018%'";
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        for(int i=0;i<1;i++){
            System.out.println(sqlUtils3.search(sql));
        }
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}
