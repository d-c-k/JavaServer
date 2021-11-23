package main.java;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Read {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase mainJavaDB = mongoClient.getDatabase("mainJava");
            MongoCollection<Document> itemsCollection = mainJavaDB.getCollection("items");

            //find one document with new Document
            Document item1 = itemsCollection.find(new Document("_id", new ObjectId("619cba828635002ba37f4064"))).first();
            System.out.println("item1: " + item1.toJson());
        }
    }
}
