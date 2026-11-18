import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class HealthApplication {
    private static final int PORT = 8080;

    private HealthApplication() {}

    public static void main(String[] args) throws IOException {
        if (args.length > 0 && "--check".equals(args[0])) {
            System.exit(checkHealth());
        }

        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        server.createContext("/health", HealthApplication::handleHealth);
        server.start();
        System.out.println("Health application listening on port " + PORT);
    }

    private static void handleHealth(HttpExchange exchange) throws IOException {
        byte[] response = "{\"status\":\"UP\"}".getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, response.length);
        try (var body = exchange.getResponseBody()) {
            body.write(response);
        }
    }

    private static int checkHealth() {
        try {
            HttpURLConnection connection =
                    (HttpURLConnection) URI.create("http://127.0.0.1:" + PORT + "/health")
                            .toURL()
                            .openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            int status = connection.getResponseCode();
            connection.disconnect();
            return status == HttpURLConnection.HTTP_OK ? 0 : 1;
        } catch (IOException exception) {
            return 1;
        }
    }
}
