package newsandtools.dingqiqi.com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/20.
 */
public class TimeUtil {

    /**
     * String 时间戳转标准时间(截取掉年)
     *
     * @return
     */
    public static String StringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Long mlong = Long.parseLong(date);

        return format.format(mlong * 1000).substring(5);
    }

}
