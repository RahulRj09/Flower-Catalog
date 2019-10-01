package flowercatalog;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.List;

class GetHandler implements HttpHandler {
    private static List<Comment> comments = new PostHandler().getComments();
    private static DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        ResultSet resultSet = databaseHelper.getAllData();
        String res = "";
        for (Comment comment : comments) {
            res += comment.getName();
            res += "  ";
            res += comment.getComment();
            res += "  ";
            res += comment.getDate();
        }
        exchange.sendResponseHeaders(200, res.length());
        OutputStream os = exchange.getResponseBody();
        os.write(res.getBytes());
        os.close();
    }
}

