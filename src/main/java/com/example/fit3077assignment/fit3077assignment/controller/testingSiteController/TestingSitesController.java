package com.example.fit3077assignment.fit3077assignment.controller.testingSiteController;

import com.example.fit3077assignment.fit3077assignment.model.testingSites.TestingSites;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * This is the testing site controller
 */
@RestController
public class TestingSitesController {
    /**
     * This is gets all testing sites
     * @return all testing sites
     * @throws IOException
     * @throws InterruptedException
     */
    @CrossOrigin
    @GetMapping("/testing-site")
    public ResponseEntity<String> getTestingSite() throws IOException, InterruptedException {
        TestingSites testingSites = TestingSites.getInstance();
        HttpResponse<String> response = testingSites.getAllTestingSites();

        if (response.statusCode() == HttpStatus.OK.value()) {
            System.out.println("we are successful before sending result back");
            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        }
        System.out.println("we are FAILING");
        return new ResponseEntity<>("No site as found", HttpStatus.BAD_REQUEST);

    }

    /**
     * This returns one test site depending on the ID
     * @param id
     * @return test site
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/testing-site/{id}")
    public ResponseEntity<String> getOneTestingSite(@PathVariable String id) throws Exception {
        TestingSites testingSites = TestingSites.getInstance();
        HttpResponse<String> response = testingSites.getOneTest(id);
        if (response.statusCode() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
    }


    }


