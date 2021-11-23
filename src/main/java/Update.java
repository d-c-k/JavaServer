package main.java;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class Update {

    public static void main(String[] args) {
        JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();

        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
            MongoDatabase mainJavaDB = mongoClient.getDatabase("mainJava");
            MongoCollection<Document> itemsCollection = mainJavaDB.getCollection("items");

            //update one document
            Bson filter = eq("_id", new ObjectId("619cba828635002ba37f4064"));
            Bson updateOperation = set("body", "updated body");
            UpdateResult updateResult = itemsCollection.updateOne(filter, updateOperation);
            System.out.println(itemsCollection.find(filter).first().toJson(prettyPrint));
            System.out.println(updateResult);
        }
    }
}
