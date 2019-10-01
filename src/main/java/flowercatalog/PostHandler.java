package flowercatalog;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class PostHandler implements HttpHandler {
    private List<Comment> comments = new ArrayList<>();
    private DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String query = bufferedReader.readLine();
        int indexOfAnd = query.indexOf("&");
        StringBuffer name = getName(query, indexOfAnd);
        StringBuffer comment = getComment(query, indexOfAnd + 9);
        Comment commentA = new Comment(name, comment, LocalDate.now());
        comments.add(commentA);
        databaseHelper.insert(commentA);
        File root = FileSystemView.getFileSystemView().getHomeDirectory();
        String path = root + "/Flower-Catalog/src/main/java/htmlpages/guestBook.html";
        File file = new File(path);
        exchange.sendResponseHeaders(200, file.length());
        try (OutputStream os = exchange.getResponseBody()) {
            Files.copy(file.toPath(), os);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private StringBuffer getName(String query, int indexOfAnd) {
        StringBuffer name = new StringBuffer();
        for (int i = 5; i < indexOfAnd; i++) {
            if (query.charAt(i) == '+') {
                name.append(" ");
            } else {
                name.append(query.charAt(i));
            }
        }
        return name;
    }

    private StringBuffer getComment(String query, int indexOfAnd) {
        StringBuffer comment = new StringBuffer();
        for (int i = indexOfAnd; i < query.length(); i++) {
            if (query.charAt(i) == '+') {
                comment.append(" ");
            } else {
                comment.append(query.charAt(i));
            }
        }
        return comment;
    }

    public List<Comment> getComments() {
        return comments;
    }
}

