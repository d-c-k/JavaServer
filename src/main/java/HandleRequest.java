package main.java;

import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HandleRequest {

    private String method;
    private URI uri;
    private String body;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getBody() {
        return body;
    }

    public void setBody(InputStream inputBody) {
        Scanner s = new Scanner(inputBody).useDelimiter("\\A");
        String body = s.hasNext() ? s.next() : "";
        this.body = body;
    }

    public void handleRequest(HttpExchange request) {
        try {
            request.sendResponseHeaders(200, 0);

            setUri(request.getRequestURI());
            setMethod(request.getRequestMethod());
            setBody(request.getRequestBody());

            OutputStream os = request.getResponseBody();

            System.out.println("Method: " + this.method);
            System.out.println("Body: " + this.body);
            System.out.println("URI: " + this.uri);

            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, String> parseQueryString(String qs) {
        Map<String, String> result = new HashMap<>();
        if (qs == null)
            return result;

        int last = 0;
        int next;
        int l = qs.length();

        while (last < l) {
            next = qs.indexOf('&', last);
            if (next == -1)
                next = l;

            if (next > last) {
                int eqPos = qs.indexOf('=', last);
                try {
                    if (eqPos < 0 || eqPos > next)
                        result.put(URLDecoder.decode(qs.substring(last, next), "utf-8"),"");
                    else
                        result.put(URLDecoder.decode(qs.substring(last, eqPos), "utf-8"), URLDecoder.decode(qs.substring(eqPos + 1, next), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            last = next + 1;
        }
        return result;
    }
}
