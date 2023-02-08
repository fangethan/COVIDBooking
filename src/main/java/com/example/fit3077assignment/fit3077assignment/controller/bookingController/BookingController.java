package com.example.fit3077assignment.fit3077assignment.controller.bookingController;

import com.example.fit3077assignment.fit3077assignment.model.booking.HomeBooking;
import com.example.fit3077assignment.fit3077assignment.model.booking.OnSiteBooking;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the booking controller to contorl the responses from the HTML for bookins
 */
@RestController
public class BookingController {
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/static/QRCode.png";

    /**
     * This gets all the bookings
     * @return all bookings
     * @throws IOException
     * @throws InterruptedException
     */
    @CrossOrigin
    @GetMapping("/booking")
    public String getBooking() throws IOException, InterruptedException {
        OnSiteBooking onSiteBooking = OnSiteBooking.getInstance();
        HttpResponse<String> response = onSiteBooking.getAllBookings();
        return response.body();
    }

    /**
     * This gets only one booking depending on the ID
     * @param id
     * @return one booking
     * @throws Exception
     */
    // id: e5d391d1-8d3e-41ed-b94b-3602649c8d86
    @GetMapping("/booking/{id}")
    public String getOneBooking(@PathVariable String id) throws Exception {
        OnSiteBooking onSiteBooking = OnSiteBooking.getInstance();
        return onSiteBooking.getOneBook(id);
    }

    /**
     * This adds bookings for on site bookings through the admin
     * @param clientID
     * @param testingSiteID
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    @CrossOrigin
    @PostMapping("/booking/onsite/{clientID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> addOnSiteBooking(@PathVariable String clientID, @PathVariable String testingSiteID, @PathVariable String date, @PathVariable String time) throws IOException, InterruptedException, ParseException {
        OnSiteBooking onSiteBooking = OnSiteBooking.getInstance();

        String startTimeBooking = date + " " +time;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date inputDate = dateFormat.parse(startTimeBooking);
        Date currentDate = new Date();
        System.out.println("currentDate");
        System.out.println(dateFormat.format(currentDate));
        currentDate = dateFormat.parse(dateFormat.format(currentDate));
        // checks if date and time is bigger or equal to timeslot
        if (inputDate.compareTo(currentDate) > 0 || inputDate.compareTo(currentDate) == 0) {
            System.out.println("date is bigger");
            HttpResponse<String> response = onSiteBooking.makeBooking(clientID,testingSiteID,date,time);
            if (response.statusCode() == HttpStatus.CREATED.value()) {
                System.out.println("in response entity");

                System.out.println(response.body());
                return new ResponseEntity<>(response.body(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin
    @PutMapping("/booking/onsite/{bookingID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> modifyOnSiteBooking(@PathVariable String bookingID, @PathVariable String testingSiteID,
                                                      @PathVariable String date, @PathVariable String time) throws IOException, InterruptedException, ParseException {
        OnSiteBooking onSiteBooking = OnSiteBooking.getInstance();
        HttpResponse<String> response = onSiteBooking.getAllBookings();

        ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
        for (ObjectNode node: jsonNodes) {
            if (node.get("id").textValue().equals(bookingID)) {
                System.out.println(node);
                if (node.get("status").textValue().equals("CANCELLED")) {
                    return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
                } else {
                    response = onSiteBooking.modifyBooking(bookingID,testingSiteID,date,time);
                    // STATUS CODE FOR SUCCESS PUT/PATCH
                    if (response.statusCode() == HttpStatus.OK.value()) {
                        System.out.println("in response entity");
                        System.out.println(response.body());
                        return new ResponseEntity<>(response.body(), HttpStatus.OK);
                    }
                }
            }
        }

        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
    }


    @CrossOrigin
    @DeleteMapping ("/booking/onsite/{bookingID}")
    public ResponseEntity<Void> deleteOnSiteBookings(@PathVariable String bookingID) throws IOException, InterruptedException {
        OnSiteBooking onSiteBooking = OnSiteBooking.getInstance();
        HttpResponse<String> response = onSiteBooking.deleteBooking(bookingID);
        // STATUS CODE FOR SUCCESS DELETE
        if (response.statusCode() == HttpStatus.NO_CONTENT.value()) {
            System.out.println("in response entity");

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin
    @PutMapping("/booking/onsite/{bookingID}")
    public ResponseEntity<String> cancelOnSiteBooking(@PathVariable String bookingID) throws IOException, InterruptedException {
        OnSiteBooking onSiteBooking = OnSiteBooking.getInstance();

        HttpResponse<String> response = onSiteBooking.cancelBooking(bookingID);
        // STATUS CODE FOR SUCCESS PUT/PATCH
        if (response.statusCode() == HttpStatus.OK.value()) {
            System.out.println("in response entity");

            System.out.println(response.body());
            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
    }
    @CrossOrigin
    @PutMapping("/booking/homeBooking/cancel/{bookingID}")
    public ResponseEntity<String> cancelHomeBooking(@PathVariable String bookingID) throws IOException, InterruptedException {
        System.out.println("controller");
        HomeBooking homebooking = HomeBooking.getInstance();
        HttpResponse<String> response = homebooking.cancelBooking(bookingID);;
        // STATUS CODE FOR SUCCESS PUT/PATCH
        if (response.statusCode() == HttpStatus.OK.value()) {
            System.out.println("in response entity");

            System.out.println(response.body());
            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin
    @DeleteMapping ("/booking/homebooking/{bookingID}")
    public ResponseEntity<Void> deleteHomeBookings(@PathVariable String bookingID) throws IOException, InterruptedException {
        System.out.println("sfsfsfs");
        HomeBooking homebooking = HomeBooking.getInstance();
        HttpResponse<String> response = homebooking.deleteBooking(bookingID);
        // STATUS CODE FOR SUCCESS DELETE
        if (response.statusCode() == HttpStatus.NO_CONTENT.value()) {
            System.out.println("in response entity");

            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin
    @PutMapping("/homebooking/modify/{bookingID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> modifyHomeBookings(@PathVariable String bookingID, @PathVariable String testingSiteID, @PathVariable String date, @PathVariable String time) throws IOException, InterruptedException, ParseException {
        System.out.println("controller");
        HomeBooking homeBooking = HomeBooking.getInstance();
        HttpResponse<String> response = homeBooking.modifyBooking(bookingID,testingSiteID,date,time);
        // STATUS CODE FOR SUCCESS PUT/PATCH
        if (response.statusCode() == HttpStatus.OK.value()) {
            System.out.println("in response entity");
            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);

    }


    /**
     * This is the method for adding home booking
     * @param clientID
     * @param testingSiteID
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    @CrossOrigin
    @PostMapping("/booking/homebooking/{clientID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> addHomeBooking(@PathVariable String clientID, @PathVariable String testingSiteID, @PathVariable String date,@PathVariable String timee) throws IOException, InterruptedException {
        System.out.println("skfghsf,jbswfksbfglksbgwksjg");
        HomeBooking homeBookings = HomeBooking.getInstance();
        HttpResponse<String> response = homeBookings.makeBooking(clientID,testingSiteID,date,timee);
        byte[] qrcode;
        if (response.statusCode() == HttpStatus.CREATED.value()) {

            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
    }

}
