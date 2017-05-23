package newsandtools.dingqiqi.com.mode;

import java.util.List;

/**
 * 笑话实体类
 * Created by dingqiqi on 2016/6/22.
 */
public class JokeMode {
    /***
     * 时间
     */
    private String ct;
    /***
     * 内容
     */
    private String text;
    /***
     * 标题
     */
    private String title;
    private int type;

    private List<JokeMode> contentlist;

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<JokeMode> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<JokeMode> contentlist) {
        this.contentlist = contentlist;
    }
}
