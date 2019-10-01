package flowercatalog;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;


import java.io.IOException;
import java.io.OutputStream;


class GetHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        JSONObject res = databaseHelper.getAllData();
        String response = res.toString();
        exchange.getResponseHeaders().set("Content-Type", "appication/json");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}