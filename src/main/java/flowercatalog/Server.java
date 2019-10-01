package flowercatalog;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<Comment> comments = new ArrayList<>();
    private static DatabaseHelper databaseHelper = new DatabaseHelper();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.createContext("/comments", new PostHandler());
        server.createContext("/getComments", new GetHandler());
        server.setExecutor(null);
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI requestURI = exchange.getRequestURI();
            File root = FileSystemView.getFileSystemView().getHomeDirectory();
            String path = root + "/Flower-Catalog/src/main/java/" + requestURI;
            File file = new File(path);
            exchange.sendResponseHeaders(200, file.length());
            try (OutputStream os = exchange.getResponseBody()) {
                Files.copy(file.toPath(), os);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static class PostHandler implements HttpHandler {


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

    }

    private static class GetHandler implements HttpHandler {

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
}


//    ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//                    try {
//                    objectMapper.writeValue(new File("/Users/rahul.joshi/Flower-Catalog/src/main/java/json/comments.json"), comments);
//                    } catch (IOException e) {
//                    e.printStackTrace();
//                    }
//
