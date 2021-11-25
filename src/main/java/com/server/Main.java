package com.server;

import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {

        int port = 3000;
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        httpServer.createContext("/", new RootHandler());

        httpServer.createContext("/api/items", new ItemsHandler());

        httpServer.start();

    }
}
