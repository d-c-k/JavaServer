package main.java;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {

        int port = 3000;
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext root = httpServer.createContext("/", new HttpHandler() {
            public void handle(HttpExchange httpExchange) {
                HandleRequest hr = new HandleRequest();
                hr.handleRequest(httpExchange);
            }
        });
        httpServer.start();

    }
}
