package newsandtools.dingqiqi.com.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/6/29.
 */
public class DateUtil {
    /**
     * 获取当前完整时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        return dateFormat.format(new Date());
    }

    /**
     * 根据时间格式获取时间
     *
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(new Date());
    }

}
