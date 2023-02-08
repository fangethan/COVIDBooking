package com.example.fit3077assignment.fit3077assignment.model.booking;

import com.example.fit3077assignment.fit3077assignment.model.generator.Generator;
import com.example.fit3077assignment.fit3077assignment.model.generator.PinGenerator;
import com.example.fit3077assignment.fit3077assignment.model.url.RootURLs;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhoneBooking extends Booking {
    private static String covidURL = RootURLs.ROOT_URL + "/covid-test";
    private static String bookingURL = RootURLs.ROOT_URL + "/booking";

    private static PhoneBooking phoneBooking =  null;

    /**
     * Get the instance of on site testing site
     */
    public static PhoneBooking getInstance() {
        if (phoneBooking == null) {
            phoneBooking = new PhoneBooking();
        }
        return phoneBooking;
    }

    private PhoneBooking() {

    }

    public HttpResponse<String> getCOVID() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(covidURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    /**
     * This gets every test site
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> getAllBookings() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(bookingURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    @Override
    public String getOneBook(String id) throws Exception {
        return null;
    }

    @Override
    public HttpResponse<String> makeBooking(String clientID, String testingSiteID, String date, String time) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public String generator(Generator generate) {
        String pinCode = generate.generate();
        System.out.println("in the phone booking class" + pinCode);
        return pinCode;
    }

    public String setJSONString(String customerID, String testingSiteID, String startTime, String pinCode, JSONObject jsonObject) {
        return "{" +
                "\"customerId\":\"" + customerID + "\"," +
                "\"testingSiteId\":\"" + testingSiteID + "\"," +
                "\"startTime\":\"" + startTime + "\"," +
                "\"notes\":\"" + pinCode + "\"," +
                "\"additionalInfo\":" + jsonObject +
                "}";
    }

    @Override
    public HttpResponse<String> modifyBooking(String bookingID, String testingSiteID, String date, String time) throws IOException, InterruptedException, ParseException {
        String jsonString = "";

        String proposedTime = date + " " +time;

        JSONObject jsonObject = new JSONObject();

//        jsonObject.put("modified", "true");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date inputDate = dateFormat.parse(proposedTime);
        Date presentDate = new Date();
        System.out.println("currentDate");
        System.out.println(dateFormat.format(presentDate));
        presentDate = dateFormat.parse(dateFormat.format(presentDate));

        // get all bookings
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(bookingURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);



        // get the booking then to set the json string
        for (ObjectNode node: jsonNodes) {
            // sets the json string
            if (node.get("id").textValue().equals(bookingID)) {
                Object modified = node.get("additionalInfo").get("modified");

                JSONArray arrayStartTime = new JSONArray();
                JSONArray arrayTestingSite = new JSONArray();

                JsonNode TestingSites = node.get("additionalInfo").get("TestingSites");
                JsonNode jsonStr = node.get("additionalInfo").get("StartTime");

                if (jsonStr.isArray()) {
                    for (final JsonNode objNode : jsonStr) {
                        arrayStartTime.put(objNode.textValue());
                    }
                }
                if (TestingSites.isArray()) {
                    for (final JsonNode objNode : TestingSites) {
                        arrayTestingSite.put(objNode.textValue());
                    }
                }
                arrayStartTime.put(proposedTime);
                arrayTestingSite.put(testingSiteID);
                jsonObject.put("modified", "true");
                jsonObject.put("StartTime", arrayStartTime);
                jsonObject.put("TestingSites", arrayTestingSite);

                PinGenerator pinGenerator = new PinGenerator();
                String pincode = generator(pinGenerator);
                System.out.println("in the modify on site booking " + pincode);

                // update testing site or time
                if ((!node.get("testingSite").get("id").textValue().equals(testingSiteID) && testingSiteID != "") &&
                        (inputDate.compareTo(presentDate) > 0)) {
                    proposedTime = date+"T"+time+":00.000Z";
                    System.out.println("both values changed");
                    jsonString = setJSONString(node.get("customer").get("id").textValue(),testingSiteID,proposedTime,pincode,jsonObject);

                } else if (!node.get("testingSite").get("id").textValue().equals(testingSiteID) && testingSiteID != "") {
                    System.out.println("testing site values changed");
                    jsonString = setJSONString(node.get("customer").get("id").textValue(),testingSiteID,node.get("startTime").textValue(),pincode,jsonObject);

                } else if (inputDate.compareTo(presentDate) > 0) {
                    System.out.println("date/time values changed");
                    proposedTime = date+"T"+time+":00.000Z";
                    jsonString = setJSONString(node.get("customer").get("id").textValue(), node.get("testingSite").get("id").textValue(),proposedTime,pincode,jsonObject);

                }
            }
        }

        System.out.println(jsonString);

        // update user information
        request = HttpRequest
                .newBuilder(URI.create(bookingURL + "/" + bookingID))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
        return response;
    }


    @Override
    public HttpResponse<String> deleteBooking(String bookingID) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public HttpResponse<String> cancelBooking(String bookingID) throws IOException, InterruptedException {
        return null;
    }

}
