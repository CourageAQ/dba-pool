import com.jdbc.utils.SqlUtils4;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author CourageAQ
 * @date 2018/11/27 10:25
 */
public class SqlUtils4Test {

    SqlUtils4 sqlUtils4 = SqlUtils4.getInstance();


    @Test
    public void select(){
        /**不带条件的查询*/
        String sql = "select  zzdrsl,zzdrsj,sfhc,hcdw from jkxxgl where convert(varchar(10),zzdrsj,23) like '2018%'";
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        for(int i=0;i<1;i++){
            System.out.println(sqlUtils4.search("datasource2",sql));
            System.out.println(sqlUtils4.search(sql));
        }

        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}
