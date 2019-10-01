package flowercatalog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;

public class DatabaseHelper {
    private Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/myDb?useSSL=false", "root", "password");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(Comment comment) {
        String sql = "insert into comment(name,comment,date) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, comment.getName());
            preparedStatement.setString(2, comment.getComment());
            preparedStatement.setDate(3, Date.valueOf(comment.getDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public JSONObject getAllData() {
        JSONObject comments = new JSONObject();

        JSONArray commentArray = new JSONArray();
        String sql = "select * from comment";
        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                JSONObject comment = new JSONObject();
                comment.put("name", resultSet.getString("name"));
                comment.put("comment", resultSet.getString("comment"));
                comment.put("date", resultSet.getDate("date").toString());
                commentArray.add(comment);
            }
            comments.put("comments", commentArray);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
}


