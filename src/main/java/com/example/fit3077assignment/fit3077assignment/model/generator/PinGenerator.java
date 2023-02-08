package com.example.fit3077assignment.fit3077assignment.model.generator;

import java.util.Random;

/**
 * This is the pin generator class that implements the genretaotr interface
 * This creates a pin code
 */
public class PinGenerator implements Generator{

    private final int PIN_CODE_LENGTH = 6;
    private final int MAX_RANGE = 10;

    /**
     * This generates a new pin code
     * @return pincode
     */
    @Override
    public String generate() {
        String pinCode = "";
        Random random = new Random();

        for (int i = 0; i < PIN_CODE_LENGTH; i++) {
            int randomNumber = random.nextInt(MAX_RANGE);
            pinCode += String.valueOf(randomNumber);
        }
        System.out.println("Pin code is: " + pinCode);
        return pinCode;
    }
}
