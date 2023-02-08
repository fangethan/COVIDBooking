package com.example.fit3077assignment.fit3077assignment.model.booking;

import com.example.fit3077assignment.fit3077assignment.model.generator.Generator;
import com.example.fit3077assignment.fit3077assignment.model.generator.QRCode;
import com.example.fit3077assignment.fit3077assignment.model.generator.URLGenerator;
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
import java.time.Instant;
import java.util.Base64;
import java.util.Date;


/**
 * This class is used to make bookings for the homebooking section and extends the booking class
 */
public class HomeBooking extends Booking {

    private static String HomebookingURL = RootURLs.ROOT_URL + "/booking";
    private static HomeBooking homeBooking = null;

    /**
     * Get the instance of home booking
     */
    public static HomeBooking getInstance() {
        if (homeBooking == null) {
            homeBooking = new HomeBooking();
        }
        return homeBooking;
    }

    private HomeBooking() {
    }

    /**
     * @return null
     */
    public HttpResponse<String> getAllBookings() throws IOException, InterruptedException {
        return null;
    }

    /**
     * @param id
     */
    public String getOneBook(String id) throws Exception {
        return null;
    }

    /**
     * This class is a make booking class for home bookings and it stores the booking with the qr code and
     * also the URl for the user.
     *
     * @param clientID
     * @param testingSiteID
     * @return response
     */
    @Override
    public HttpResponse<String> makeBooking(String clientID, String testingSiteID, String date, String time) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String currentTime = Instant.now().toString();
        currentTime = currentTime.substring(0, currentTime.length() - 4) + "Z";

        byte[] qrCodeFinal = new byte[0];
        URLGenerator url = new URLGenerator();
        String clientURL = generator(url);


        //This Generators the qr code
        QRCode qrCodeGenerator = new QRCode();
        try {
            qrCodeFinal = qrCodeGenerator.getQRCodeImage(clientID, 80, 80);

        } catch (Exception e) {

        }

        //This is used to decode the byte array of the QR to a base 64 so it can be displayed
        String base64QR = Base64.getEncoder().encodeToString(qrCodeFinal);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("URL", clientURL);
        jsonObject.put("QRCode", base64QR);

        String StartTimeBooking = date + "T" + time + ":00.000Z";
        JSONArray arrayTime = new JSONArray();
        arrayTime.put(StartTimeBooking);

        JSONArray arrayTestingSides = new JSONArray();
        arrayTestingSides.put(testingSiteID);


        jsonObject.put("StartTime", arrayTime);
        jsonObject.put("TestingSites",arrayTestingSides);


        /**
         * This creates the jsonstring and stores the url and qr code in the additionalinfo
         */
        String jsonString = setJSONString(clientID,testingSiteID,StartTimeBooking, jsonObject);

        HttpRequest request = HttpRequest
                .newBuilder(URI.create(HomebookingURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .header("Content-Type", "application/json") // This header needs to be set when sending a JSON request body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        return response;
    }

    public String setJSONString(String customerID, String testingSiteID, String startTime, JSONObject jsonObject) {
        return "{" +
                "\"customerId\":\"" + customerID + "\"," +
                "\"testingSiteId\":\"" + testingSiteID + "\"," +
                "\"startTime\":\"" + startTime + "\"," +
                "\"additionalInfo\":" + jsonObject +
                "}";
    }

    @Override
    public HttpResponse<String> modifyBooking(String bookingID, String testingSiteID, String date, String time) throws IOException, InterruptedException, ParseException {

        String jsonString = "";

        String proposedTime = date + " " + time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date inputDate = dateFormat.parse(proposedTime);
        Date presentDate = new Date();
        presentDate = dateFormat.parse(dateFormat.format(presentDate));

        proposedTime = date + "T" + time + ":00.000Z";
        // get all bookings
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(HomebookingURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = new JSONObject();
        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);


        // get the booking then to set the json string
        for (ObjectNode node : jsonNodes) {
            // sets the json string
            if (node.get("id").textValue().equals(bookingID)) {
                // update testing site or time
                proposedTime = date + "T" + time + ":00.000Z";
                JSONArray arrayStartTime = new JSONArray();
                JSONArray arrayTestingSite = new JSONArray();

                Object URL = node.get("additionalInfo").get("URL");
                Object QRCode = node.get("additionalInfo").get("URL");

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

                jsonObject.put("URL", URL);
                jsonObject.put("QRCode", QRCode);
                jsonObject.put("StartTime", arrayStartTime);
                jsonObject.put("TestingSites", arrayTestingSite);

                if ((!node.get("testingSite").get("id").textValue().equals(testingSiteID) && testingSiteID != "") &&
                        (inputDate.compareTo(presentDate) > 0)) {

                    jsonString = setJSONString(node.get("customer").get("id").textValue(), testingSiteID, proposedTime, jsonObject);
                    System.out.println(jsonString);
                } else if (!node.get("testingSite").get("id").textValue().equals(testingSiteID) && testingSiteID != "") {
                    System.out.println("testing site values changed");
                    jsonString = setJSONString(node.get("customer").get("id").textValue(), testingSiteID, node.get("startTime").textValue(), jsonObject);

                } else if (inputDate.compareTo(presentDate) > 0) {
                    System.out.println("date/time values changed");
                    proposedTime = date + "T" + time + ":00.000Z";
                    jsonString = setJSONString(node.get("customer").get("id").textValue(), node.get("testingSite").get("id").textValue(), proposedTime, jsonObject);

                }
            }
        }


        // update user information
        request = HttpRequest
                .newBuilder(URI.create(HomebookingURL + "/" + bookingID))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .header("Content-Type", "application/json") // This header needs to be set when sending a JSON request body.
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    @Override
    public HttpResponse<String> deleteBooking(String bookingID) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(HomebookingURL + "/" + bookingID))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

    @Override
    public HttpResponse<String> cancelBooking(String bookingID) throws IOException,InterruptedException {
        String jsonString = "";
        // update user information

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(HomebookingURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
        // get the booking then to set the json string
        for (ObjectNode node: jsonNodes) {
            // sets the json string
            if (node.get("id").textValue().equals(bookingID) && node.get("status").textValue().equals("INITIATED")) {
                jsonString = "{" +
                        "\"customerId\":\"" + node.get("customer").get("id").textValue() + "\"," +
                        "\"testingSiteId\":\"" + node.get("testingSite").get("id").textValue() + "\"," +
                        "\"startTime\":\"" + node.get("startTime").textValue() + "\"," +
                        "\"status\":\"" + "CANCELLED" + "\"" +
                        "}";
            }
        }
        request = HttpRequest
                .newBuilder(URI.create(HomebookingURL + "/" + bookingID))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        return response;
    }


    /**
     * This Generates a urlcode
     *
     * @param generate
     * @return urlcode
     */
    @Override
    public String generator(Generator generate) {
        String urlCode = generate.generate();
        return urlCode;

    }
}