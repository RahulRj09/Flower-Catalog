package flowercatalog;

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

    public String getAllData() {
        String sql = "select * from comment";
        String res = "";
        try (Connection connection = this.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                res += resultSet.getString("name");
                res += "  ";
                res += resultSet.getString("comment");
                res += "  ";
                res += resultSet.getDate("date");
                res += "\n";
            }
            System.out.println(res);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

}


