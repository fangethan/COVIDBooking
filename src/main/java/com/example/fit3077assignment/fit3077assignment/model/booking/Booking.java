package com.example.fit3077assignment.fit3077assignment.model.booking;

import com.example.fit3077assignment.fit3077assignment.model.generator.Generator;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.ParseException;

/**
 * This is the abstract booking class used for making bookings for customers
 */
abstract class Booking {
    /**
     * gets all bookings
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract HttpResponse<String> getAllBookings() throws IOException, InterruptedException;
    /**
     * gets allone booking based on ID
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract String getOneBook(String id) throws Exception;
    /**
     * This make sthe booking
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract HttpResponse<String> makeBooking(String clientID, String testingSiteID, String date, String time) throws IOException,InterruptedException;
    /**
     * This calls the generator interface (Factory method)
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract String generator(Generator generate);

    public abstract HttpResponse<String> modifyBooking(String clientID, String testingSiteID, String date, String time) throws IOException,InterruptedException, ParseException;

    public abstract HttpResponse<String> deleteBooking(String bookingID) throws IOException,InterruptedException;

    public abstract HttpResponse<String> cancelBooking(String bookingID) throws IOException,InterruptedException;


    }
