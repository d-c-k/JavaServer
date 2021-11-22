package main.java;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class ItemsHandler implements HttpHandler {

    @Override

    public void handle(HttpExchange he) throws IOException {
        URI uri = he.getRequestURI();

        String fullPath = uri.getPath();
        String subPath = fullPath.substring(0, fullPath.lastIndexOf("items") + 5);
        String lastBit = getLastBitFromURI(fullPath);

        if (!Objects.equals(subPath, "/api/items")) {
            he.sendResponseHeaders(400, 0);
            he.close();
        }
        else if (isNumeric(lastBit)) {
            int id = Integer.parseInt(lastBit);

            if (Objects.equals(he.getRequestMethod(), "GET")) {
                he.sendResponseHeaders(200, 0);
                System.out.println("get item " + id);
                he.close();
            }
            else if (Objects.equals(he.getRequestMethod(), "POST")) {
                he.sendResponseHeaders(200, 0);
                System.out.println("edit item " + id);
                he.close();
            }
            else if (Objects.equals(he.getRequestMethod(), "DELETE")) {
                he.sendResponseHeaders(200, 0);
                System.out.println("delete item " + id);
                he.close();
            }
            else {
                he.sendResponseHeaders(400, 0);
                he.close();
            }
        }
        else if (Objects.equals(lastBit, "items")) {
                if (Objects.equals(he.getRequestMethod(), "GET")) {
                    he.sendResponseHeaders(200, 0);
                    System.out.println("get all items");
                    he.close();
                } else if (Objects.equals(he.getRequestMethod(), "POST")) {
                    he.sendResponseHeaders(201, 0);
                    System.out.println("created new item");
                    he.close();
                } else {
                    he.sendResponseHeaders(400, 0);
                    System.out.println("bad request");
                    he.close();
                }
        }
        else {
            he.sendResponseHeaders(404, 0);
            he.close();
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String getLastBitFromURI(String uri) {
        return uri.replaceFirst(".*/([^/?]+).*", "$1");
    }
}
