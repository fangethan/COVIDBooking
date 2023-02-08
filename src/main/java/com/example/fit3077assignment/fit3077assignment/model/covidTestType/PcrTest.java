package com.example.fit3077assignment.fit3077assignment.model.covidTestType;

/**
 * PcrTest sees if user needs to collect a pcr test or not
 * implements the CovidTestingType interface
 */
public class PcrTest implements CovidTestingType {

    /**
     * This checks to see if value determines if user should get a pcr
     * @param value
     * @return boolean
     */
    @Override
    public Boolean getCovidTest(String value) {
        int checkValue = Integer.parseInt(value);
        if (checkValue == 3) {
            return true;
        }
        return false;
    }
}


// The strategy method was also used make a set of algorithms
// This was used through our onsite testing class which links to a Covidtype Test interface in which
// the onsite could either choose to use a Rat Test or a PCR, making the choice interchangeable.
