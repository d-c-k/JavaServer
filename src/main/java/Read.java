package main.java;

import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class Read {

    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase mainJavaDB = mongoClient.getDatabase("mainJava");
            MongoCollection<Document> itemsCollection = mainJavaDB.getCollection("items");

            //find one document with new Document
            Document item1 = itemsCollection.find(new Document("_id", new ObjectId("619cba828635002ba37f4064"))).first();
            System.out.println("Find one with new Document:");
            System.out.println(item1.toJson());

            //find one document with Filters.eq()
            Document item2 = itemsCollection.find(eq("_id", new ObjectId("619cc1fb5b8a8d5857829410"))).first();
            System.out.println("Find one with Filters.eq():");
            System.out.println(item2.toJson());

            //get all
            MongoCursor<Document> cursor = itemsCollection.find().iterator();
            System.out.println("Get all:");
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
    }
}
