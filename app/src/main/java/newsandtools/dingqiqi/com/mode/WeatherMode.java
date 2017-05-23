package newsandtools.dingqiqi.com.mode;

import java.util.List;

/**
 * Created by dingqiqi on 2016/7/1.
 * 天气mode
 */
public class WeatherMode {

//    "date": "2016-07-01",
//            "week": "星期五",
//            "curTemp": "31℃",
//            "aqi": null,
//            "fengxiang": "南风",
//            "fengli": "3-4级",
//            "hightemp": "34℃",
//            "lowtemp": "27℃",
//            "type": "多云",
//            "index": [

    private String date;
    private String week;
    private String curTemp;
    private String aqi;
    private String fengxiang;
    private String fengli;
    private String hightemp;
    private String lowtemp;
    private String type;
    private List<WeatherIndexMode> index;

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

    public String getCurTemp() {
        return curTemp;
    }

    public void setCurTemp(String curTemp) {
        this.curTemp = curTemp;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
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

    public List<WeatherIndexMode> getIndex() {
        return index;
    }

    public void setIndex(List<WeatherIndexMode> index) {
        this.index = index;
    }
}
