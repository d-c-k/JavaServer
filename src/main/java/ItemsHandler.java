package main.java;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.models.Item;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class ItemsHandler implements HttpHandler {

    @Override

    public void handle(HttpExchange he) throws IOException {
        try (MongoClient mongoClient = MongoClients.create(new DBConnection().connectionSettings())) {
            MongoDatabase mainJavaDB = mongoClient.getDatabase("mainJava");
            MongoCollection<Item> items = mainJavaDB.getCollection("items", Item.class);

            URI uri = he.getRequestURI();

            String fullPath = uri.getPath();
            String subPath = fullPath.substring(0, fullPath.lastIndexOf("items") + 5);
            String lastBit = getLastBitFromURI(fullPath);

            InputStream is = he.getRequestBody();
            OutputStream os = he.getResponseBody();

            if (!Objects.equals(subPath, "/api/items")) {
                he.sendResponseHeaders(400, 0);
                he.close();
            }
            else if (Objects.equals(lastBit, "items")) {
                    // Read all
                    if (Objects.equals(he.getRequestMethod(), "GET")) {
                        he.sendResponseHeaders(200, 0);
                        ArrayList<Item> responseItems = new ArrayList<Item>();

                        MongoCursor<Item> cursor = items.find().iterator();

                        while (cursor.hasNext()) {
                            responseItems.add(cursor.next());
                        }

                        GsonUtils gson = new GsonUtils();
                        String json = gson.getGson().toJson(responseItems);

                        os.write(json.getBytes());
                        he.close();
                    }
                    // Create new
                    else if (Objects.equals(he.getRequestMethod(), "POST")) {
                        String input = parseInput(is);
                        JsonObject json = new JsonParser().parse(input).getAsJsonObject();

                        if (Objects.equals(json.get("title").getAsString(), "")) {
                            he.sendResponseHeaders(400, 0);
                            String errorMsg = "Todo-item requires a title";
                            os.write(errorMsg.getBytes());
                            he.close();
                        }
                        else if (Objects.equals(json.get("body").getAsString(), "")){
                            he.sendResponseHeaders(400, 0);
                            String errorMsg = "Todo-item requires a body";
                            os.write(errorMsg.getBytes());
                            he.close();
                        }
                        else {
                            he.sendResponseHeaders(201, 0);

                            Item newItem = new Item().setId(new ObjectId())
                                    .setTitle(json.get("title").getAsString())
                                    .setBody(json.get("body").getAsString())
                                    .setCreatedAt(new Date())
                                    .setUpdatedAt(new Date());
                            items.insertOne(newItem);

                            he.close();
                        }
                    } else {
                        he.sendResponseHeaders(400, 0);
                        he.close();
                    }
            }
            else {
                ObjectId id = new ObjectId(lastBit);

                // Get single item
                if (Objects.equals(he.getRequestMethod(), "GET")) {
                    Item item = items.find(eq("_id", id)).first();

                    if (item == null) {
                        he.sendResponseHeaders(404, 0);
                    }
                    else {
                        he.sendResponseHeaders(200, 0);
                        GsonUtils gson = new GsonUtils();
                        String json = gson.getGson().toJson(item);
                        os.write(json.getBytes());
                    }
                    he.close();
                }
                // Edit single item
                else if (Objects.equals(he.getRequestMethod(), "POST")) {
                    String input = parseInput(is);
                    JsonObject json = new JsonParser().parse(input).getAsJsonObject();

                    if (Objects.equals(json.get("title").getAsString(), "")) {
                        he.sendResponseHeaders(400, 0);
                        String errorMsg = "Todo-item requires a title";
                        os.write(errorMsg.getBytes());
                        he.close();
                    }
                    else if (Objects.equals(json.get("body").getAsString(), "")){
                        he.sendResponseHeaders(400, 0);
                        String errorMsg = "Todo-item requires a body";
                        os.write(errorMsg.getBytes());
                        he.close();
                    }
                    else {
                        he.sendResponseHeaders(202, 0);

                        Bson query = eq("_id", id);

                        Bson updates = Updates.combine(
                                Updates.set("title", json.get("title").getAsString()),
                                Updates.set("body", json.get("body").getAsString()),
                                Updates.set("updatedAt", new Date()));

                        items.updateOne(query, updates);
                        he.close();
                    }
                }
                // Delete single item
                else if (Objects.equals(he.getRequestMethod(), "DELETE")) {
                    Item item = items.find(eq("_id", id)).first();

                    if (item == null) {
                        he.sendResponseHeaders(404, 0);
                    }
                    else {
                        he.sendResponseHeaders(200, 0);
                        Bson query = eq("_id", id);
                        items.deleteOne(query);
                    }
                    he.close();
                }
                else {
                    he.sendResponseHeaders(400, 0);
                    he.close();
                }
            }
        } catch (Exception e) {
            he.sendResponseHeaders(500, 0);
            he.close();
        }
    }

    private static String getLastBitFromURI(String uri) {
        return uri.replaceFirst(".*/([^/?]+).*", "$1");
    }

    private static String parseInput(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        String input = s.hasNext() ? s.next() : "";
        return input;
    }
}
