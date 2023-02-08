package com.example.fit3077assignment.fit3077assignment.controller.bookingController;

import com.example.fit3077assignment.fit3077assignment.model.booking.PhoneBooking;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.ParseException;

@RestController
public class PhoneController {
    /**
     * This gets all the covid tests from the onesiteTesting class
     *
     * @return OnSiteTesting().getAllCOVID()
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/phoneBooking/covidtest/{bookingID}/{pincode}")
    public ResponseEntity<String> getCOVID(@PathVariable String bookingID, @PathVariable String pincode) throws Exception {
        PhoneBooking phoneBooking = PhoneBooking.getInstance();
        HttpResponse<String> response = phoneBooking.getCOVID();
        if (response.statusCode() == HttpStatus.OK.value()) {
            ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
            for (ObjectNode node: jsonNodes) {
                if (node.get("booking").get("id").textValue().equals(bookingID) && node.get("booking").get("notes").textValue().equals(pincode)) {
                    return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<String>(response.body(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/booking/onsite/checkPending/{bookingID}")
    public ResponseEntity<String> checkPending(@PathVariable String bookingID) throws IOException, InterruptedException {
        PhoneBooking checkPending = PhoneBooking.getInstance();
        HttpResponse<String> response = checkPending.getCOVID();
        if (response.statusCode() == HttpStatus.OK.value()) {
            ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
            for (ObjectNode node: jsonNodes) {
                if (node.get("booking").get("id").textValue().equals(bookingID)) {
                    return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<>(response.body(), HttpStatus.OK);
    }

    /**
     * This gets all the bookings
     *
     * @return all bookings
     * @throws IOException
     * @throws InterruptedException
     */
    @CrossOrigin
    @GetMapping("/phoneBooking/book/{bookingID}/{pincode}")
    public ResponseEntity<String> getBooking(@PathVariable String bookingID, @PathVariable String pincode) throws IOException, InterruptedException {
        PhoneBooking phoneBooking = PhoneBooking.getInstance();
        HttpResponse<String> response = phoneBooking.getAllBookings();
        if (response.statusCode() == HttpStatus.OK.value()) {
            ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
            for (ObjectNode node: jsonNodes) {
                if (node.get("id").textValue().equals(bookingID) && node.get("notes").textValue().equals(pincode)) {
                    return new ResponseEntity<String>(response.body(), HttpStatus.OK);
                }
            }
        }
        System.out.println("invalid input");
        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
    }

    /**
     * This adds bookings for on site bookings through the admin
     * @param testingSiteID
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    @CrossOrigin
    @PutMapping("/phoneBooking/modify/{bookingID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> modifyPhoneBooking(@PathVariable String bookingID, @PathVariable String testingSiteID, @PathVariable String date, @PathVariable String time) throws IOException, InterruptedException, ParseException {
        PhoneBooking phoneBooking = PhoneBooking.getInstance();
        HttpResponse<String> response = phoneBooking.modifyBooking(bookingID, testingSiteID, date, time);
        // STATUS CODE FOR SUCCESS PUT/PATCH
        if (response.statusCode() == HttpStatus.OK.value()) {
            System.out.println("in response entity");
            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);

    }



}
