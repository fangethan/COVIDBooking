package com.example.fit3077assignment.fit3077assignment.model.covidTestType;

/**
 * RatTest sees if user needs to collect a rat test or not
 * implements the CovidTestingType interface
 */
public class RatTest implements CovidTestingType {
    /**
     * This checks to see if value determines if user should get a rat
     * @param value
     * @return boolean
     */
    @Override
    public Boolean getCovidTest(String value) {
        int checkValue = Integer.parseInt(value);
        if (checkValue <= 2) {
            return true;
        }
        return false;
    }
}
