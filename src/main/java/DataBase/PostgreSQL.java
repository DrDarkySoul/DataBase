package DataBase;

import Objects.ForumJSON;
import Objects.UserJSON;
import Objects.ThreadJSON;
import Other.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.sql.*;
import java.util.Objects;

/**
 * Created by Rishat_Valitov on 06.03.17.
 */

public class PostgreSQL {

    public interface Callback {
        void onSuccess();
        void onError(int err);
    }

    private Connection postgreSQL;

    public PostgreSQL(){
        getConnection();
    }

    private Connection getConnection() {
        postgreSQL = null;
        try {

            String user = "technopark_user";
            String url = "jdbc:postgresql://localhost:5432/technopark";
            String password = "technogeek";

            postgreSQL = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return postgreSQL;
    }

    public String createForum(ForumJSON input,
                                  Callback callback) {
        JSONObject answer = new JSONObject();

        if(!input.isAnyFieldEmpty()) {
            PreparedStatement preparedStatement = null;
            String querySelectForm = "SELECT * FROM forum WHERE lower(slug) = lower(?) OR lower(user_nickname) = lower(?)";

            try {
                preparedStatement = postgreSQL.prepareStatement(querySelectForm);
                preparedStatement.setString(1, input.getSlug());
                preparedStatement.setString(2, input.getUser());
                ResultSet queryResult = preparedStatement.executeQuery();

                if(queryResult.isBeforeFirst()) {
                    answer = Service.ResultSetToJSONObject(queryResult);
                    String nick = answer.getString("user_nickname");
                    answer.put("user", nick);
                    callback.onError(HttpURLConnection.HTTP_CONFLICT);
                    return answer.toString();
                } else {
                    String querySelectUser = "SELECT * FROM users WHERE lower(nickname) = lower(?)";

                    preparedStatement = postgreSQL.prepareStatement(querySelectUser);
                    preparedStatement.setString(1, input.getUser());
                    queryResult = preparedStatement.executeQuery();

                    if(!queryResult.isBeforeFirst()) {
                        callback.onError(HttpURLConnection.HTTP_NOT_FOUND);
                        return answer.toString();
                    } else {
                        JSONObject user = Service.ResultSetToJSONObject(queryResult);
                        input.setUser(user.getString("nickname"));
                        String insertQuery = "INSERT INTO forum(slug, title, user_nickname) VALUES (?,?,?);";
                        preparedStatement = postgreSQL.prepareStatement(insertQuery);

                        preparedStatement.setString(1, input.getSlug());
                        preparedStatement.setString(2, input.getTitle());
                        preparedStatement.setString(3, input.getUser());

                        answer = input.toJSON();
                        preparedStatement.executeQuery();
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            callback.onError(HttpURLConnection.HTTP_CONFLICT);
            return "";
        }
        callback.onSuccess();
        return answer.toString();
    }

    public String getForumInfo(String input,
                              Callback callback) {

        JSONObject answerObject = new JSONObject();

        PreparedStatement preparedStatement = null;

        String query = "SELECT * FROM forum WHERE lower(slug) = lower(?)";

        try {
            preparedStatement = postgreSQL.prepareStatement(query);
            preparedStatement.setString(1, input);
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.isBeforeFirst()) {
                answerObject = Service.ResultSetToJSONObject(queryResult);
                String user = answerObject.getString("user_nickname");
                answerObject.put("user", user);
                callback.onSuccess();
                return answerObject.toString();
            } else {
                callback.onError(HttpURLConnection.HTTP_NOT_FOUND);
                return answerObject.toString();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        callback.onError(HttpURLConnection.HTTP_NOT_FOUND);
        return answerObject.toString();
    }

    public String createUser(UserJSON input,
                                 Callback callback) {
        JSONObject answerObject = new JSONObject();

        if(!input.isAnyFieldEmpty()) {

            PreparedStatement preparedStatement = null;

            String query = "SELECT * FROM users WHERE lower(nickname) = lower(?) OR lower(email) = lower(?)";

            try {
                preparedStatement = postgreSQL.prepareStatement(query);
                preparedStatement.setString(1, input.getNickname());
                preparedStatement.setString(2, input.getEmail());
                ResultSet queryResult = preparedStatement.executeQuery();

                if(queryResult.isBeforeFirst()) {
                    JSONArray answerArray = Service.ResultSetToJSONArray(queryResult);
                    callback.onError(HttpURLConnection.HTTP_CONFLICT);
                    return answerArray.toString();
                } else {
                    String insertQuery = "INSERT INTO users(about, email, fullname, nickname) VALUES (?,?,?,?);";
                    preparedStatement = postgreSQL.prepareStatement(insertQuery);

                    preparedStatement.setString(1, input.getAbout());
                    preparedStatement.setString(2, input.getEmail());
                    preparedStatement.setString(3, input.getFullname());
                    preparedStatement.setString(4, input.getNickname());

                    answerObject = input.toJSON();
                    preparedStatement.executeQuery();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            callback.onError(HttpURLConnection.HTTP_CONFLICT);
            return answerObject.toString();
        }
        callback.onSuccess();
        return answerObject.toString();
    }

    public String getUserInfo(String input,
                             Callback callback) {

        JSONObject answerObject = new JSONObject();

        PreparedStatement preparedStatement = null;

        String query = "SELECT * FROM users WHERE lower(nickname) = lower(?)";

        try {
            preparedStatement = postgreSQL.prepareStatement(query);
            preparedStatement.setString(1, input);
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.isBeforeFirst()) {
                answerObject = Service.ResultSetToJSONObject(queryResult);
                callback.onSuccess();
                return answerObject.toString();
            } else {
                callback.onError(HttpURLConnection.HTTP_NOT_FOUND);
                return answerObject.toString();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        callback.onError(HttpURLConnection.HTTP_NOT_FOUND);
        return answerObject.toString();
    }

    public String updateUser(UserJSON input,
                             Callback callback) {

        JSONObject answer = new JSONObject();

        if(!input.isAnyFieldEmpty()) {

            PreparedStatement preparedStatement = null;
            String query = "SELECT * FROM users WHERE lower(nickname) = lower(?)";

            try {
                preparedStatement = postgreSQL.prepareStatement(query);
                preparedStatement.setString(1, input.getNickname());
                ResultSet queryResult = preparedStatement.executeQuery();

                if(!queryResult.isBeforeFirst()) {
                    callback.onError(HttpURLConnection.HTTP_NOT_FOUND);
                    return answer.toString();
                } else {
                    JSONObject clientNote = Service.ResultSetToJSONObject(queryResult);

                    if(!Objects.equals(input.getEmail(), "")) {
                        query = "SELECT * FROM users WHERE lower(nickname) <> lower(?) AND lower(email) = lower(?);";
                        preparedStatement = postgreSQL.prepareStatement(query);
                        preparedStatement.setString(1, input.getNickname());
                        preparedStatement.setString(2, input.getEmail());
                        queryResult = preparedStatement.executeQuery();

                        if (queryResult.isBeforeFirst()) {
                            callback.onError(HttpURLConnection.HTTP_CONFLICT);
                            return answer.toString();
                        }
                    }

                        String updateQuery = "UPDATE users SET email = ?, about = ?, fullname = ? WHERE nickname = ? ;";
                        preparedStatement = postgreSQL.prepareStatement(updateQuery);
                        if(!Objects.equals(input.getEmail(), "")) {
                            preparedStatement.setString(1, input.getEmail());
                            clientNote.put("email", input.getEmail());
                        } else {
                            preparedStatement.setString(1, clientNote.getString("email"));
                        }
                        if(!Objects.equals(input.getAbout(), "")) {
                            preparedStatement.setString(2, input.getAbout());
                            clientNote.put("about", input.getAbout());
                        } else {
                            preparedStatement.setString(2, clientNote.getString("about"));
                        }
                        if(!Objects.equals(input.getFullname(), "")) {
                            preparedStatement.setString(3, input.getFullname());
                            clientNote.put("fullname", input.getFullname());
                        } else {
                            preparedStatement.setString(3, clientNote.getString("fullname"));
                        }
                        preparedStatement.setString(4, input.getNickname());

                        answer = clientNote;
                        preparedStatement.executeQuery();
                    }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            callback.onError(HttpURLConnection.HTTP_CONFLICT);
            return answer.toString();
        }
        callback.onSuccess();
        return answer.toString();
    }

    public String createThread(String forumSlug,
                               ThreadJSON input,
                               Callback callback) {
        JSONObject answer = new JSONObject();

        if(!input.isAnyFieldEmpty()) {
            PreparedStatement preparedStatement = null;
            String querySelectForm = "SELECT * FROM forum WHERE lower(slug) = lower(?)";

            try {
                preparedStatement = postgreSQL.prepareStatement(querySelectForm);
                preparedStatement.setString(1, forumSlug);
                ResultSet queryResult = preparedStatement.executeQuery();

                if(!queryResult.isBeforeFirst()) {
                    callback.onError(HttpURLConnection.HTTP_NOT_FOUND);
                    return answer.toString();
                } else {
                    String querySelectUser = "SELECT * FROM users WHERE lower(nickname) = lower(?)";

                    preparedStatement = postgreSQL.prepareStatement(querySelectUser);
                    preparedStatement.setString(1, input.getAuthor());
                    queryResult = preparedStatement.executeQuery();

                    if(!queryResult.isBeforeFirst()) {
                        callback.onError(HttpURLConnection.HTTP_NOT_FOUND);
                        return answer.toString();
                    } else {
                        String querySelectThread = "SELECT * FROM thread WHERE lower(author) = lower(?) AND lower(forum) = lower(?)";

                        preparedStatement = postgreSQL.prepareStatement(querySelectThread);
                        preparedStatement.setString(1, input.getAuthor());
                        preparedStatement.setString(2, input.getForum());
                        queryResult = preparedStatement.executeQuery();

                        if(queryResult.isBeforeFirst()) {
                            callback.onError(HttpURLConnection.HTTP_CONFLICT);
                            return answer.toString();
                        } else {
                            String insertQuery = "INSERT INTO thread(author, created, forum, message, title) VALUES (?,?,?,?,?);";
                            preparedStatement = postgreSQL.prepareStatement(insertQuery);

                            preparedStatement.setString(1, input.getAuthor());
                            preparedStatement.setString(2, input.getCreated());
                            preparedStatement.setString(3, input.getForum());
                            preparedStatement.setString(4, input.getMessage());
                            preparedStatement.setString(5, input.getTitle());

                            answer = input.toJSON();
                            preparedStatement.executeQuery();
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            callback.onError(HttpURLConnection.HTTP_CONFLICT);
            return "";
        }
        callback.onSuccess();
        return answer.toString();
    }
}
