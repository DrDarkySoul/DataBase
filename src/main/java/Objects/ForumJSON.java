package Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

/**
 * Created by Rishat_Valitov on 06.03.17.
 */

public class ForumJSON {
    private String posts;
    private String slug;
    private String threads;
    private String title;
    private String user;

    @JsonCreator
    public ForumJSON(
            @JsonProperty("posts") String posts,
            @JsonProperty("slug") String slug,
            @JsonProperty("threads") String threads,
            @JsonProperty("title") String title,
            @JsonProperty("user") String user)
    {
        if(posts == null) {
            this.posts = "0";
        } else {
            this.posts = posts;
        }

        if(threads == null) {
            this.threads = "0";
        } else {
            this.threads = threads;
        }

        if(title == null) {
            this.title = "";
        } else {
            this.title = title;
        }

        this.slug = slug;
        this.user = user;
    }

    public String getPosts() {
        return this.posts;
    }
    public String getSlug() {
        return this.slug;
    }
    public String getThreads() {
        return this.threads;
    }
    public String getTitle() {
        return this.title;
    }
    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Boolean isAnyFieldEmpty() {
        return (this.slug == null ||
                this.user == null );
    }

    public JSONObject toJSON() {
        JSONObject answer = new JSONObject();
        answer.put("slug", this.slug);
        answer.put("title", this.title);
        answer.put("user", this.user);

        return answer;
    }
}
