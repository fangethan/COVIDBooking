package com.example.fit3077assignment.fit3077assignment.model.covidTesting;

import com.example.fit3077assignment.fit3077assignment.model.covidTestType.PcrTest;
import com.example.fit3077assignment.fit3077assignment.model.covidTestType.RatTest;
import com.example.fit3077assignment.fit3077assignment.model.url.RootURLs;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This is for on site testing which generates creates a covid test
 */
public class CovidTesting {

    private static String covidURL = RootURLs.ROOT_URL + "/covid-test";
    private static CovidTesting covidTesting =  null;

    /**
     * Get the instance of on site testing site
     */
    public static CovidTesting getInstance() {
        if (covidTesting == null) {
            covidTesting = new CovidTesting();
        }
        return covidTesting;
    }
    //    The Singleton design pattern was shown in classes OnSiteTesting, Login, OnSiteBooking, HomeBooking and TestingSites.
//    This creates a private object of the class and makes our constructor private to ensure no other class can create the object.
//    We can call the method by first getting an instance of the singleton class through the static method getInstance().
//    This allows us to then call the other methods in those classes.
    private CovidTesting() {
    }

    /**
     * This gets all the current covid tests
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    public String getAllCOVID() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(covidURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    /**
     * This gets ones specfic covid test based on the ID
     * @param id
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    public String getCOVID(String id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(covidURL + "/" + id))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();

    }

    /**
     * This one creates a new covid test
     * @param testType
     * @param clientID
     * @param adminID
     * @param bookingID
     * @return repsonse
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> createCovidTest(String testType, String clientID, String adminID, String bookingID) throws IOException,InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        if (new RatTest().getCovidTest(testType) == true) {
            testType = "RAT";
        } else if (new PcrTest().getCovidTest(testType) == true) {
            testType = "PCR";
        }
        System.out.println("Test type: " + testType);
        String jsonString = "{" +
                "\"type\":\"" + testType + "\"," +
                "\"patientId\":\"" + clientID + "\"," +
                "\"administerId\":\"" + adminID + "\"," +
                "\"bookingId\":\"" + bookingID + "\"" +
                "}";
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(covidURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

}
