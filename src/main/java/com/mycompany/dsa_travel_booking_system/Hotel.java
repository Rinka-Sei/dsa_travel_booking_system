/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dsa_travel_booking_system;

public class Hotel {

    private String hotelId;
    private String name;
    private String location;
    private double rating;
    private double pricePerNight;

    public Hotel(String hotelId, String name, String location, double rating, double pricePerNight) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.rating = rating;
        this.pricePerNight = pricePerNight;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public double getRating() {
        return rating;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}
