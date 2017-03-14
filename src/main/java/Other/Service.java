package Other;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Rishat_Valitov on 06.03.17.
 */

public class Service {
    public static JSONArray ResultSetToJSONArray(ResultSet resultSet){
        JSONArray jsonArray = new JSONArray();
        try {
            while (resultSet.next()) {
                Integer total_rows = resultSet.getMetaData().getColumnCount();
                JSONObject obj = new JSONObject();
                for (Integer i = 0; i < total_rows; i++) {
                    obj.put(resultSet.getMetaData().getColumnLabel(i + 1)
                            .toLowerCase(), resultSet.getObject(i + 1));
                }
                jsonArray.put(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return jsonArray;
    }

    public static JSONObject ResultSetToJSONObject(ResultSet resultSet){
        JSONObject jsonObject = new JSONObject();
        try {
            if(resultSet.next()) {
                int total_rows = resultSet.getMetaData().getColumnCount();
                for (int i = 0; i < total_rows; i++) {
                    jsonObject.put(resultSet.getMetaData().getColumnLabel(i + 1)
                            .toLowerCase(), resultSet.getObject(i + 1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return jsonObject;
    }
}
