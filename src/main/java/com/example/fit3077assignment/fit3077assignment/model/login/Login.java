package com.example.fit3077assignment.fit3077assignment.model.login;

import com.example.fit3077assignment.fit3077assignment.model.url.RootURLs;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This is the login class which allows for users to log in as either admin, receptionist or
 */
public class Login {

    private static String userUrl = RootURLs.ROOT_URL + "/user";
    private static Login login =  null;
    /**
     * Get the instance of login
     */
    public static Login getInstance() {
        if (login == null) {
            login = new Login();
        }
        return login;
    }
    private Login() {}

    /**
     * This method gets all the usernames
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> getAllUsername() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(userUrl))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;

    }

    /**
     * This gets a username based on the ID
     * @param id
     * @return response
     * @throws Exception
     */
    public HttpResponse<String> getUsername(String id) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(userUrl + "/" + id))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    /**
     * This logins into thhe system
     * @param username
     * @param password
     * @param jwt
     * @return response
     * @throws IOException
     * @throws InterruptedException
     */
    public HttpResponse<String> login(String username, String password, String jwt) throws IOException, InterruptedException {

        String jsonString = "{" +
                "\"userName\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"" +
                "}";
        System.out.println("jwt value is:" + jwt);
        String loginUrl = userUrl + "/login";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(loginUrl + "?jwt=" + jwt))
                .setHeader("Authorization", RootURLs.getMyApiKey())
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }



}
