package main.java.models;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class Delete {

    public static void main(String[] args) {


        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase mainJavaDB = mongoClient.getDatabase("mainJava");
            MongoCollection<Document> itemsCollection = mainJavaDB.getCollection("items");

            Bson filter = eq("_id", new ObjectId("619cc218f305446b8b7fcbf1"));
            DeleteResult result = itemsCollection.deleteOne(filter);
            System.out.println(result);
        }
    }
}
