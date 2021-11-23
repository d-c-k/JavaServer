package main.java;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class RootHandler implements HttpHandler {

    @Override

    public void handle(HttpExchange he) throws IOException {
        URI uri = he.getRequestURI();

        String path = uri.getPath();

        if(Objects.equals(he.getRequestMethod(), "GET") && Objects.equals(path, "/")) {
            he.sendResponseHeaders(200, 0);
            System.out.println("Todo-list");
            he.close();
        }
        else {
            he.sendResponseHeaders(400, 0);
            he.close();
        }
    }
}
