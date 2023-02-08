package com.example.fit3077assignment.fit3077assignment.controller.testingSiteController;

import com.example.fit3077assignment.fit3077assignment.model.covidTesting.CovidTesting;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

/**
 * This is the on site tstig controller between html and java
 */
@RestController
public class CovidTestingController {
    /**
     * This gets all the covid tests from the onesiteTesting class
     * @return OnSiteTesting().getAllCOVID()
     * @throws Exception
     */
    @GetMapping("/covid-test")
    public String getCOVID() throws Exception {
        CovidTesting covidTesting = CovidTesting.getInstance();
        return covidTesting.getAllCOVID();
    }
    /**
     * This gets one the covid tests based on ID from the onesiteTesting class
     * @return OnSiteTesting().getCOVID(id)
     * @throws Exception
     */
    @GetMapping("/covid-test/{id}")
    public String getOneCOVID(@PathVariable String id) throws Exception {
        CovidTesting covidTesting = CovidTesting.getInstance();
        return covidTesting.getCOVID(id);
    }

    /**
     * This gets all onsite testing based on test type, client ID, admin ID and booking ID
     * @param testType
     * @param clientID
     * @param adminID
     * @param bookingID
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/testing-site/onsite/{testType}/{clientID}/{adminID}/{bookingID}")
    public ResponseEntity<String> createCovidTest(@PathVariable String testType, @PathVariable String clientID, @PathVariable String adminID, @PathVariable String bookingID) throws Exception {
        CovidTesting covidTesting = CovidTesting.getInstance();
        HttpResponse<String> response = covidTesting.createCovidTest(testType,clientID,adminID,bookingID);
        if (response.statusCode() == HttpStatus.CREATED.value()) {
            System.out.println("successful");
            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);

    }

}
