package newsandtools.dingqiqi.com.mode;

/**
 * 聊天实体类
 * Created by dingqiqi on 2016/6/24.
 */
public class ChatMode {

    private String time;
    private String code;
    private String text;
    /**
     * 0 返回 1 自己
     */
    private int type;
    /**
     * 是否显示历史消息红线
     */
    private boolean isHint = false;

    public boolean isHint() {
        return isHint;
    }

    public void setHint(boolean hint) {
        isHint = hint;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
