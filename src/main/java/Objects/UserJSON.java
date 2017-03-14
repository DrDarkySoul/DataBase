package Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;
import org.json.JSONObject;

/**
 * Created by Rishat_Valitov on 13.03.17.
 */

public class UserJSON {

    private String about;
    private String email;
    private String fullname;
    private String nickname;

    @JsonCreator
    public UserJSON(
            @JsonProperty("about") String about,
            @JsonProperty("email") String email,
            @JsonProperty("fullname") String fullname,
            @JsonProperty("nickname") String nickname)
    {
        if(about == null) {
            this.about = "";
        } else {
            this.about = about;
        }

        if(email == null) {
            this.email = "";
        } else {
            this.email = email;
        }

        if(fullname == null) {
            this.fullname = "";
        } else {
            this.fullname = fullname;
        }

        if(nickname == null) {
            this.nickname = "";
        } else {
            this.nickname = nickname;
        }
    }

    public UserJSON(String nickname) {
        this.email = "";
        this.nickname = nickname;
        this.fullname = "";
        this.about = "";
    }

    public UserJSON() {}

    public String getAbout() {
            return this.about;
        }
    public String getEmail() {
            return this.email;
        }
    public String getFullname() {
            return this.fullname;
        }
    public String getNickname() {
            return this.nickname;
        }

    public Boolean isAnyFieldEmpty() {
        return (this.nickname == null);
    }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public JSONObject toJSON() {
        JSONObject answer = new JSONObject();
        answer.put("about", this.about);
        answer.put("email", this.email);
        answer.put("fullname", this.fullname);
        answer.put("nickname", this.nickname);

        return answer;
    }
}

