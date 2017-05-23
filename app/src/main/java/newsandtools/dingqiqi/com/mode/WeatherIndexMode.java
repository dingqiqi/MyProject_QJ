package newsandtools.dingqiqi.com.mode;

/**
 * Created by 丁奇奇 on 2016/7/1.
 * 各种指数
 */
public class WeatherIndexMode {
//    "name": "感冒指数",
//            "code": "gm",
//            "index": "",
//            "details": "各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。",
//            "otherName": ""

    //感冒指数
    private String name;
    private String code;
    //等级
    private String index;
    //详情
    private String details;
    private String otherName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }
}
