package com.ef.newlead.location;

/**
 * Created by seanzhou on 8/12/16.
 */
public class LocationInfo {
    private String city;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String provider;

    public LocationInfo(String city, double latitude, double longitude, String provider) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provider = provider;
    }

    public LocationInfo(){

    }

    public LocationInfo setCity(String city) {
        this.city = city;
        return this;
    }

    public LocationInfo setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public LocationInfo setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public LocationInfo setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getCity() {
        return city;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getProvider() {
        return provider;
    }
}
