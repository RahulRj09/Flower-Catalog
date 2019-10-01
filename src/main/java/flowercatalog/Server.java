package flowercatalog;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.util.List;

public class Server {

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


    }


//    ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//                    try {
//                    objectMapper.writeValue(new File("/Users/rahul.joshi/Flower-Catalog/src/main/java/json/comments.json"), comments);
//                    } catch (IOException e) {
//                    e.printStackTrace();
//                    }
//
