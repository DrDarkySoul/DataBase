package Controllers;

import DataBase.PostgreSQL;
import Objects.ForumJSON;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
                              HttpSession httpSession, HttpServletResponse response) {
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
}
