package Controllers;

import DataBase.PostgreSQL;
import Objects.UserJSON;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;

/**
 * Created by Rishat_Valitov on 06.03.17.
 */

@RestController
@RequestMapping(path = "/api/user")
public class UserController {
    private final PostgreSQL dataBase;

    UserController() {
            dataBase = new PostgreSQL();
        }

    @RequestMapping(path = "/{userNickname}/create",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json")
    public String createUser(@RequestBody UserJSON body,
                             HttpServletResponse response,
                             @PathVariable("userNickname") String nickname) {
        body.setNickname(nickname);
        return dataBase.createUser(body, new PostgreSQL.Callback() {
            @Override
            public void onSuccess() {
                response.setStatus(HttpURLConnection.HTTP_CREATED);
                }
            @Override
            public void onError(int err) {
                response.setStatus(err);
                }
            });
        }

    @RequestMapping(path = "/{userNickname}/profile",
            method = RequestMethod.GET,
            produces = "application/json")
    public String getUserInfo(HttpServletResponse response,
                             @PathVariable("userNickname") String nickname) {
        return dataBase.getUserInfo(nickname, new PostgreSQL.Callback() {
            @Override
            public void onSuccess() {
                response.setStatus(HttpURLConnection.HTTP_OK);
            }
            @Override
            public void onError(int err) {
                response.setStatus(err);
            }
        });
    }

    @RequestMapping(path = "/{userNickname}/profile",
            method = RequestMethod.POST,
            produces = "application/json",
            consumes = "application/json")
    public String updateUser(@RequestBody UserJSON body,
                             HttpServletResponse response,
                             @PathVariable("userNickname") String nickname) {
        body.setNickname(nickname);
        return dataBase.updateUser(body, new PostgreSQL.Callback() {
            @Override
            public void onSuccess() {
                response.setStatus(HttpURLConnection.HTTP_OK);
            }
            @Override
            public void onError(int err) {
                response.setStatus(err);
            }
        });
    }
}

