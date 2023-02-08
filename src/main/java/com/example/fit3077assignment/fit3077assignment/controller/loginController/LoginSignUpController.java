package com.example.fit3077assignment.fit3077assignment.controller.loginController;

import com.example.fit3077assignment.fit3077assignment.model.login.Login;
import com.example.fit3077assignment.fit3077assignment.model.login.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;

/**
 * This is the login controller which controlls the html responses and login
 */
@RestController
public class LoginSignUpController {


    /**
     * This method gets all users
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping("/users")
    public String getAllUsers() throws IOException, InterruptedException {
        Login login = Login.getInstance();
        HttpResponse<String> response = login.getAllUsername();
        return response.body();
    }

    /**
     * This is gets only one user
     * @param id
     * @return response
     * @throws Exception
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<String> getOneUser(@PathVariable String id) throws Exception {
        Login login = Login.getInstance();
        HttpResponse<String> response = login.getUsername(id);
        if (response.statusCode() == HttpStatus.OK.value()) {
//            System.out.println("we are successful before sending result back");
            return new ResponseEntity<>(response.body(), HttpStatus.OK);
        } else {
            System.out.println("we are FAILING before sending result back");
            return new ResponseEntity<>("Username/password incorrect!", HttpStatus.BAD_REQUEST);
        }
    }

    // cross origin enables us to cross origin resource sharing only for this specific method
    // allows us
    // javascript cannot connect to the api in the same domain
    // locally, both front end in jquery, and java api endpoint are both in localhost
    // so we use cross origin to connnect them over

    /**
     * This authenticates to see if the login is valid. Checks if they ar a customer or admine and such
     * @param username
     * @param password
     * @param jwt
     * @param userInfo
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    @CrossOrigin
    @PostMapping("/users/{username}/{password}/{jwt}/{userInfo}")
    public ResponseEntity<User> authenticate(@PathVariable String username, @PathVariable String password, @PathVariable String jwt, @PathVariable String userInfo) throws IOException, InterruptedException {
        Login login = Login.getInstance();
        String userType = userInfo;
        if (userType == null) {
            userType = "";
        }

        User currentUser = null;
        System.out.println("can we go this far");
        HttpResponse<String> response = login.login(username, password, jwt);
        if (response.statusCode() == HttpStatus.OK.value()) {
            System.out.println("we are successful before sending result back");

            response = login.getAllUsername();
            ObjectNode[] jsonNodes = new ObjectMapper().readValue(response.body(), ObjectNode[].class);
            for (ObjectNode node: jsonNodes) {
                if (node.get("userName").textValue().equals(username)) {
                    System.out.println(node);
                    System.out.println("user info" + userType);
                    if (userType.equals("customer") && node.get("isCustomer").booleanValue() == true) {
                        currentUser = new User(node.get("id").textValue(), node.get("userName").textValue(),
                                node.get("givenName").textValue(), node.get("familyName").textValue(), node.get("phoneNumber").textValue(), null, "true" );
                        System.out.println("current user is a customer or not: " + currentUser.getIsCustomer());
                    } else if (userType.equals("receptionist") && node.get("isReceptionist").booleanValue() == true) {
                        currentUser = new User(node.get("id").textValue(), node.get("userName").textValue(),
                                node.get("givenName").textValue(), node.get("familyName").textValue(), node.get("phoneNumber").textValue(), null, "false" );
                    } else if (userType.equals("hcw") && node.get("isHealthcareWorker").booleanValue() == true) {
                        currentUser = new User(node.get("id").textValue(), node.get("userName").textValue(),
                                node.get("givenName").textValue(), node.get("familyName").textValue(), node.get("phoneNumber").textValue(), null, "false" );
                    } else {
                        currentUser = new User(node.get("id").textValue(), node.get("userName").textValue(),
                                node.get("givenName").textValue(), node.get("familyName").textValue(), node.get("phoneNumber").textValue(), null, "null" );
                    }
                }
            }
            return new ResponseEntity<User>(currentUser, HttpStatus.OK);
        } else {
            System.out.println("we are FAILING before sending result back");
            return new ResponseEntity<User>(currentUser, HttpStatus.BAD_REQUEST);
        }
    }






}
