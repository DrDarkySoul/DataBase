package Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by Rishat_Valitov on 15.03.17.
 */
public class ThreadJSON {
    private String author;
    private String created;
    private String forum;
    private String message;
    private String title;

    @JsonCreator
    public ThreadJSON(
            @JsonProperty("autor") String author,
            @JsonProperty("created") String created,
            @JsonProperty("forum") String forum,
            @JsonProperty("message") String message,
            @JsonProperty("title") String title)
    {
        if(author == null) {
            this.author = "";
        } else {
            this.author = author;
        }

        if(created == null) {
            this.created = "";
        } else {
            this.created = created;
        }

        if(forum == null) {
            this.forum = "";
        } else {
            this.forum = forum;
        }

        if(message == null) {
            this.message = "";
        } else {
            this.message = message;
        }

        if(title == null) {
            this.title = "";
        } else {
            this.title = title;
        }
    }

    public ThreadJSON() {}

    public String getAuthor() {
        return this.author;
    }
    public String getCreated() {
        return this.created;
    }
    public String getForum() {
        return this.forum;
    }
    public String getMessage() {
        return this.message;
    }
    public String getTitle() {
        return this.title;
    }

    public Boolean isAnyFieldEmpty() {
        return (Objects.equals(this.author, "") &&
                Objects.equals(this.forum, ""));
    }


    public JSONObject toJSON() {
        JSONObject answer = new JSONObject();
        answer.put("author", this.author);
        answer.put("created", this.created);
        answer.put("forum", this.forum);
        answer.put("message", this.message);
        answer.put("title", this.title);

        return answer;
    }
}
