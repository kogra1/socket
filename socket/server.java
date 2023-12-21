import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class server {
    private static StringBuilder clientMessages = new StringBuilder();

    public static void main(String[] args) throws IOException {
        int port = 60000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/hello", new HelloHandler());

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop(0);
            System.out.println("Server stopped");
        }));

        System.out.println("Server is on port " + port + ", The address is http://localhost:60000/hello");
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                handlePostRequest(exchange);
            }

            sendResponse(exchange);
        }

        private void handlePostRequest(HttpExchange exchange) throws IOException {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            try (BufferedReader br = new BufferedReader(isr)) {
                String clientMessage = br.readLine();
                System.out.println("Client Message Received: " + clientMessage);
                clientMessages.append("\nClient Message Received:").append(clientMessage);
            }
        }

        private void sendResponse(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                String response = "Hello, Dr.Yu." + "\n" + clientMessages.toString();
                os.write(response.getBytes());
            }

            exchange.close();
        }
    }
}
