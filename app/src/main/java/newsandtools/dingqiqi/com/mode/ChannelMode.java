package newsandtools.dingqiqi.com.mode;

/**
 * Created by Administrator on 2016/6/17.
 */
public class ChannelMode {
    /**
     * 频道名称
     */
    private String name;
    /**
     * 频道值
     */
    private String value;

    public ChannelMode() {
    }

    public ChannelMode(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
