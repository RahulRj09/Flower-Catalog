package flowercatalog;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;


class GetHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        String res = databaseHelper.getAllData();
        exchange.sendResponseHeaders(200, res.length());
        OutputStream os = exchange.getResponseBody();
        os.write(res.getBytes());
        os.close();
    }
}