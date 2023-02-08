package com.example.fit3077assignment.fit3077assignment.model.booking;

import com.example.fit3077assignment.fit3077assignment.model.generator.Generator;
import com.example.fit3077assignment.fit3077assignment.model.generator.PinGenerator;
import com.example.fit3077assignment.fit3077assignment.model.url.RootURLs;
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

/**
 * This class is used to make bookings for the On site bookings section and extends the booking class
 */
public class OnSiteBooking extends Booking{

    private static String bookingURL = RootURLs.ROOT_URL + "/booking";
    private static OnSiteBooking onSiteBooking =  null;
    /**
     * Get the instance of on site booking
     */
    public static OnSiteBooking getInstance() {
        if (onSiteBooking == null) {
            onSiteBooking = new OnSiteBooking();
        }
        return onSiteBooking;
    }

    private OnSiteBooking() {
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

    /**
     * This gets one test site id
     * @return String
     * @throws Exception
     */
    public String getOneBook(String id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(bookingURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Booking at");
        System.out.println(response.body());

        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
        for (ObjectNode node: jsonNodes) {
            if (node.get("id").textValue().equals(id)) {
                System.out.println(node);
                return node.get("id").textValue();
            }
        }

        return "id not found";
    }

    /**
     * This gets every test site
     * @param clientID
     * @param testingSiteID
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> makeBooking(String clientID, String testingSiteID, String date, String time) throws IOException,InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String startBookingTime =  date + "T" + time + ":00.000Z";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("modified", "false");

        JSONArray arrayTime = new JSONArray();
        arrayTime.put(startBookingTime);

        JSONArray arrayTestingSides = new JSONArray();
        arrayTestingSides.put(testingSiteID);


        jsonObject.put("StartTime", arrayTime);
        jsonObject.put("TestingSites",arrayTestingSides);

        PinGenerator pinGenerator = new PinGenerator();
        String pincode = generator(pinGenerator);

        //This is the json string used to post the new booking
        String jsonString = setJSONString(clientID,testingSiteID,startBookingTime,pincode,jsonObject);

        System.out.println(jsonString);
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(bookingURL))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println(response.statusCode());

        return response;

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

    public HttpResponse<String> modifyBooking(String bookingID, String testingSiteID, String date, String time) throws IOException, InterruptedException, ParseException {
        String jsonString = "";

        String proposedTime = date + " " +time;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("modified", "true");

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

                PinGenerator pinGenerator = new PinGenerator();
                String pincode = generator(pinGenerator);
                System.out.println("in the modify on site booking " + pincode);

                // update testing site or time
                if ((!node.get("testingSite").get("id").textValue().equals(testingSiteID) && testingSiteID != "") &&
                        (inputDate.compareTo(presentDate) > 0)) {
                    proposedTime = date+"T"+time+":00.000Z";
                    System.out.println("both values changed");
                    System.out.println(proposedTime);

                    jsonString = setJSONString(node.get("customer").get("id").textValue(),testingSiteID,proposedTime,pincode,jsonObject);
                } else if (!node.get("testingSite").get("id").textValue().equals(testingSiteID) && testingSiteID != "") {
                    System.out.println("testing site values changed");
                    jsonString = setJSONString(node.get("customer").get("id").textValue(),testingSiteID,node.get("startTime").textValue(),pincode,jsonObject);
                } else if (inputDate.compareTo(presentDate) > 0) {
                    System.out.println("date/time values changed");
                    proposedTime = date+"T"+time+":00.000Z";
                    jsonString = setJSONString(node.get("customer").get("id").textValue(),node.get("testingSite").get("id").textValue(),proposedTime,pincode,jsonObject);
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
    public HttpResponse<String> cancelBooking(String bookingID) throws IOException,InterruptedException {
        String jsonString = "";
        // update user information

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
                .newBuilder(URI.create(bookingURL + "/" + bookingID))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        return response;
    }


    // HAS NOT BEEN TESTED YET
    // WILL APPLY ABSTRACT METHODS AND THAT LATER
    @Override
    public HttpResponse<String> deleteBooking(String bookingID) throws IOException,InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(bookingURL + "/" + bookingID))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        System.out.println(response.statusCode());

        return response;

    }


    /**
     * This generates a pincode from the PinGenerator class
     * @param generate
     * @return pinCode
     */
    @Override
    public String generator(Generator generate) {
        String pinCode = generate.generate();
        System.out.println("in the onsite booking class" + pinCode);
        return pinCode;
    }
}
