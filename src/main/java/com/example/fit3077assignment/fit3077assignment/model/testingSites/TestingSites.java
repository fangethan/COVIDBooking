package com.example.fit3077assignment.fit3077assignment.model.testingSites;

import com.example.fit3077assignment.fit3077assignment.model.url.RootURLs;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This class gets all the test sites
 */
public class TestingSites {

    private static String testingSiteURL = RootURLs.ROOT_URL + "/testing-site";
    private static TestingSites testingSites =  null;

    /**
     * Get the instance of testing site
     */
    public static TestingSites getInstance() {
        if (testingSites == null) {
            testingSites = new TestingSites();
        }
        return testingSites;
    }

    private TestingSites() {
    }

    /**
     * This gets every test site
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> getAllTestingSites() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(testingSiteURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

    /**
     * This gets only one test site depending on the ID
     * @param id
     * @return response
     * @throws Exception
     */
    public HttpResponse<String> getOneTest(String id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(testingSiteURL + "/" + id))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

}



