package main.java;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class Create {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {

            MongoDatabase mainJavaDB = mongoClient.getDatabase("mainJava");
            MongoCollection<Document> itemsCollection = mainJavaDB.getCollection("items");

            Document item = new Document("_id", new ObjectId());
            item    .append("title", "java-test-title1")
                    .append("body", "test-body")
                    .append("createdAt", new Date())
                    .append("updatedAt", new Date());

            itemsCollection.insertOne(item);
        }
    }
}
