package newsandtools.dingqiqi.com.mode;

import java.util.List;

/**
 * Created by 丁奇奇 on 2016/7/1.
 * 未来几天天气
 */
public class WeatherForecastMode {
    /*"date": "2016-07-02",
            "week": "星期六",
            "fengxiang": "南风",
            "fengli": "微风级",
            "hightemp": "33℃",
            "lowtemp": "27℃",
            "type": "雷阵雨"*/

    private String date;
    private String week;
    private String fengxiang;
    private String fengli;
    private String hightemp;
    private String lowtemp;
    private String type;

    private List<WeatherForecastMode> forecast;

    public List<WeatherForecastMode> getForecast() {
        return forecast;
    }

    public void setForecast(List<WeatherForecastMode> forecast) {
        this.forecast = forecast;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public String getFengli() {
        return fengli;
    }

    public void setFengli(String fengli) {
        this.fengli = fengli;
    }

    public String getHightemp() {
        return hightemp;
    }

    public void setHightemp(String hightemp) {
        this.hightemp = hightemp;
    }

    public String getLowtemp() {
        return lowtemp;
    }

    public void setLowtemp(String lowtemp) {
        this.lowtemp = lowtemp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
