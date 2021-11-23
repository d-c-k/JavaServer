package main.java;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.sun.net.httpserver.*;
import org.bson.Document;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String connectionString = System.getProperty("mongodb.uri");
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
            System.out.println("Available databases:");
            databases.forEach(db -> System.out.println(db.toJson()));
        }

        int port = 3000;
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        httpServer.createContext("/", new RootHandler());

        httpServer.createContext("/api/items", new ItemsHandler());

        httpServer.start();

    }
}
