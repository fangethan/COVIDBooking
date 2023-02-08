package com.example.fit3077assignment.fit3077assignment.model.url;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RootURLs {

    public static final String ROOT_URL = "https://fit3077.com/api/v2";
    public static String getMyApiKey() {
        try (InputStream input = RootURLs.class.getClassLoader().getResourceAsStream("configuration.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println("my key is:" + prop.getProperty("api_key"));
            return prop.getProperty("api_key");


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "no api key found";
    }

}
