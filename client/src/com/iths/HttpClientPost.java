package com.iths;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class HttpClientPost {

    public static void main( String[] args ) throws Exception {
        System.out.println("test");
            HttpClientPost ClientPost = new HttpClientPost();
                ClientPost.post();
        System.out.println("test2");
    }

    private static void post() throws Exception {
        HttpClient client = HttpClient.newBuilder().build();
        System.out.println("test3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/action_page"))
                .POST(HttpRequest.BodyPublishers.ofString("hej"))
                .build();
        System.out.println("test4");
        HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println(response.statusCode());
        System.out.println("test5");
    }
}









