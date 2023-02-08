package com.example.fit3077assignment.fit3077assignment.model.login;

public class User {
    private String id;
    private String username;
    private String givenName;
    private String familyName;
    private String phoneNumber;
    private String testType;
    private String isCustomer;

    /**
     * constructor of the user
     * @param id
     * @param givenName
     * @param familyName
     * @param username
     * @param phoneNumber
     * @param isCustomer
     * @param testType
     */
    public User(String id, String username, String givenName, String familyName, String phoneNumber, String testType, String isCustomer) {
        setId(id);
        setUsername(username);
        setGivenName(givenName);
        setFamilyName(familyName);
        setPhoneNumber(phoneNumber);
        setTestType(testType);
        setCustomer(isCustomer);
    }

    /**
     * getIsCustomer gets if user is a customer or not
     * @return isCustomer
     */
    public String getIsCustomer() {
        return isCustomer;
    }
    /**
     * setCustomer sets if user is a customer or not
     * @param customer
     */
    public void setCustomer(String customer) {
        this.isCustomer = customer;
    }
    /**
     * getID gets user id
     * @return id
     */
    public String getId() {
        return id;
    }
    /**
     * setID sets the user's id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * getUsername gets the user's username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * setUsername sets the username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * getGivenName gets the user given name
     * @return givenName
     */
    public String getGivenName() {
        return givenName;
    }
    /**
     * setGivenName sets the given name
     * @param givenName
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }
    /**
     * getFamilyName gets user family name
     * @return familyName
     */
    public String getFamilyName() {
        return familyName;
    }
    /**
     * setFamilyName sets user family name
     * @param familyName
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
    /**
     * getPhoneNumber gets phone number
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * setPhoneNumber sets user phone number
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /**
     * getTestType gets user covid test type
     * @return testType
     */
    public String getTestType() {
        return testType;
    }
    /**
     * setTestType sets user covid test type
     * @param testType
     */
    public void setTestType(String testType) {
        this.testType = testType;
    }
}
