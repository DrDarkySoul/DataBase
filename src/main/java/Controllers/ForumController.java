package Controllers;

import DataBase.PostgreSQL;
import Objects.ForumJSON;
import Objects.ThreadJSON;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Rishat_Valitov on 06.03.17.
 */

@RestController
@RequestMapping(path ="/api/forum")
public class ForumController {
    private final PostgreSQL dataBase;

    ForumController() {
        dataBase = new PostgreSQL();
    }

    @RequestMapping(path = "/create",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json")
    public String createForum(@RequestBody ForumJSON body,
                              HttpServletResponse response) {
        return (dataBase.createForum(body, new PostgreSQL.Callback() {
            @Override
            public void onSuccess() {
                response.setStatus(HttpURLConnection.HTTP_CREATED);
            }

            @Override
            public void onError(int err) {
                response.setStatus(err);
            }
        }));
    }

    @RequestMapping(path = "/{forumSlug}/create",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json")
    public String createThread(@PathVariable("forumSlug") String slug,
                               @RequestBody ThreadJSON body,
                               HttpServletResponse response) {
        return (dataBase.createThread(slug, body, new PostgreSQL.Callback() {
            @Override
            public void onSuccess() {
                response.setStatus(HttpURLConnection.HTTP_CREATED);
            }

            @Override
            public void onError(int err) {
                response.setStatus(err);
            }
        }));
    }

    @RequestMapping(path = "{forumSlug}/details",
            method = RequestMethod.GET,
            produces = "application/json")
    public String getForumInfo(@PathVariable("forumSlug") String slug,
                               HttpServletResponse response) {
        return (dataBase.getForumInfo(slug, new PostgreSQL.Callback() {
            @Override
            public void onSuccess() {
                response.setStatus(HttpURLConnection.HTTP_OK);
            }

            @Override
            public void onError(int err) {
                response.setStatus(err);
            }
        }));
    }
}
