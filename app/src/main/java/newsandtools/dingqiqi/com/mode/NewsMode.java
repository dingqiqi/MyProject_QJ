package newsandtools.dingqiqi.com.mode;

import java.util.List;

/**
 * 新闻mode
 * Created by dingqiqi on 2016/6/20.
 */
public class NewsMode {

    private String title;
    private String url;
    private String img;
    private String author;
    private String time;

    private List<NewsMode> article;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NewsMode> getArticle() {
        return article;
    }

    public void setArticle(List<NewsMode> article) {
        this.article = article;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
