package com.example.fit3077assignment.fit3077assignment.controller.userFacadeController;

import com.example.fit3077assignment.fit3077assignment.controller.bookingController.PhoneController;
import com.example.fit3077assignment.fit3077assignment.model.login.User;
import com.example.fit3077assignment.fit3077assignment.controller.testingSiteController.CovidTestingController;
import com.example.fit3077assignment.fit3077assignment.controller.testingSiteController.TestingSitesController;
import com.example.fit3077assignment.fit3077assignment.controller.bookingController.BookingController;
import com.example.fit3077assignment.fit3077assignment.controller.loginController.LoginSignUpController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
public class UserFacadeController {

    LoginSignUpController loginSignUpController = new LoginSignUpController();
    TestingSitesController testingSitesController = new TestingSitesController();
    CovidTestingController covidTestingController = new CovidTestingController();
    BookingController bookingController = new BookingController();
    PhoneController phoneController = new PhoneController();

    @GetMapping("/users/loginController")
    public String getUser() throws IOException, InterruptedException {
        return loginSignUpController.getAllUsers();
    }

    @GetMapping("/users/loginController/{id}")
    public ResponseEntity<String> getUserID(@PathVariable String id) throws Exception {
        return loginSignUpController.getOneUser(id);
    }

    @CrossOrigin
    @PostMapping("/users/loginController/{username}/{password}/{jwt}/{userInfo}")
    public ResponseEntity<User> authenticate(@PathVariable String username, @PathVariable String password, @PathVariable String jwt, @PathVariable String userInfo) throws IOException, InterruptedException {
        System.out.println(username);
        return loginSignUpController.authenticate(username, password,jwt, userInfo);
    }

    @CrossOrigin
    @GetMapping("/testing-site/facilityController")
    public ResponseEntity<String> getTestingSite() throws IOException, InterruptedException {
        return testingSitesController.getTestingSite();
    }

    @CrossOrigin
    @GetMapping("/testing-site/facilityController/{id}")
    public ResponseEntity<String> getOneTestingSite(@PathVariable String id) throws Exception {
        return testingSitesController.getOneTestingSite(id);
    }

    @GetMapping("/covid-test/onsiteTestController")
    public String getCOVIDTest() throws Exception {
        return covidTestingController.getCOVID();
    }

    @GetMapping("/covid-test/onsiteTestController/{id}")
    public String getOneCOVIDTest(@PathVariable String id) throws Exception {
        return covidTestingController.getOneCOVID(id);
    }

    @CrossOrigin
    @PostMapping("/testing-site/onsiteTestController/{testType}/{clientID}/{adminID}/{bookingID}")
    public ResponseEntity<String> setCovidTest(@PathVariable String testType, @PathVariable String clientID,
                                                      @PathVariable String adminID, @PathVariable String bookingID) throws Exception {
        return covidTestingController.createCovidTest(testType,clientID,adminID,bookingID);
    }

    @CrossOrigin
    @GetMapping("/booking/bookingController")
    public String getAllBooking() throws IOException, InterruptedException {
        return bookingController.getBooking();
    }

    @GetMapping("/booking/bookingController/{id}")
    public String getOneBookingID(@PathVariable String id) throws Exception {
        return bookingController.getOneBooking(id);
    }

    @CrossOrigin
    @PostMapping("/booking/onsite/bookingController/{clientID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> addOnSiteBookID(@PathVariable String clientID, @PathVariable String testingSiteID, @PathVariable String date, @PathVariable String time)
            throws IOException, InterruptedException, ParseException {
        return bookingController.addOnSiteBooking(clientID,testingSiteID, date, time);
    }

    @CrossOrigin
    @PostMapping("/booking/homebooking/bookingController/{clientID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> addHomeBookID(@PathVariable String clientID, @PathVariable String testingSiteID, @PathVariable String date,@PathVariable String time)
            throws IOException, InterruptedException {
        return bookingController.addHomeBooking(clientID,testingSiteID,date,time);
    }

    @CrossOrigin
    @PutMapping("/booking/onsite/bookingController/{bookingID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> modifyOnSiteBook(@PathVariable String bookingID, @PathVariable String testingSiteID,
                                                      @PathVariable String date, @PathVariable String time) throws IOException, InterruptedException, ParseException {
        return bookingController.modifyOnSiteBooking(bookingID,testingSiteID,date,time);
    }

    @CrossOrigin
    @DeleteMapping ("/booking/onsite/bookingController/{bookingID}")
    public ResponseEntity<Void> deleteOnSiteBook(@PathVariable String bookingID) throws IOException, InterruptedException {
        return bookingController.deleteOnSiteBookings(bookingID);
    }

    @CrossOrigin
    @PutMapping("/booking/onsite/bookingController/{bookingID}")
    public ResponseEntity<String> cancelOnSiteBook(@PathVariable String bookingID) throws IOException, InterruptedException {
        return bookingController.cancelOnSiteBooking(bookingID);
    }

    @CrossOrigin
    @PutMapping("/booking/homeBooking/bookingController/cancel/{bookingID}")
    public ResponseEntity<String> cancelHomeBook(@PathVariable String bookingID) throws IOException, InterruptedException {
        return bookingController.cancelHomeBooking(bookingID);
    }

    @CrossOrigin
    @DeleteMapping ("/booking/homebooking/bookingController/{bookingID}")
    public ResponseEntity<Void> deleteHomeBook(@PathVariable String bookingID) throws IOException, InterruptedException {
        return bookingController.deleteHomeBookings(bookingID);
    }
    @CrossOrigin
    @PutMapping("/homebooking/modify/bookingController/{bookingID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> modifyHomeBook(@PathVariable String bookingID, @PathVariable String testingSiteID, @PathVariable String date, @PathVariable String time) throws IOException, InterruptedException, ParseException {
        return bookingController.modifyHomeBookings(bookingID,testingSiteID,date,time);
    }

    @CrossOrigin
    @GetMapping("/phoneBooking/phoneController/covidtest/{bookingID}/{pincode}")
    public ResponseEntity<String> getCOVIDPhoneTest(@PathVariable String bookingID, @PathVariable String pincode) throws Exception {
        return phoneController.getCOVID(bookingID, pincode);
    }
    @CrossOrigin
    @GetMapping("/booking/onsite/phoneController/checkPending/{bookingID}")
    public ResponseEntity<String> checkPend(@PathVariable String bookingID) throws IOException, InterruptedException {
        return phoneController.checkPending(bookingID);
    }

    @CrossOrigin
    @GetMapping("/phoneBooking/book/phoneController/{bookingID}/{pincode}")
    public ResponseEntity<String> getPhoneBook(@PathVariable String bookingID, @PathVariable String pincode) throws IOException, InterruptedException {
        return phoneController.getBooking(bookingID,pincode);
    }

    @CrossOrigin
    @PutMapping("/phoneBooking/modify/phoneController/{bookingID}/{testingSiteID}/{date}/{time}")
    public ResponseEntity<String> modifyPhoneBook(@PathVariable String bookingID, @PathVariable String testingSiteID, @PathVariable String date, @PathVariable String time) throws IOException, InterruptedException, ParseException {
        return phoneController.modifyPhoneBooking(bookingID,testingSiteID,date,time);
    }

}








