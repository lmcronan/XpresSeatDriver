package com.leanza.xpresseat_driver.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by leanza on 28/04/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Van {
    private String name;
    private String driver;
    private String capacity;
    private String departuretime;
    private String reservedseat;
    private String vacantseat;
    private String route;
    private String email;
    private String notify;
    private String status;
    private String passenger;


    public Van() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDeparturetime() {
        return departuretime;
    }

    public void setDeparturetime(String departuretime) {
        this.departuretime = departuretime;
    }

    public String getReservedseat() {
        return reservedseat;
    }

    public void setReservedseat(String reservedseat) {
        this.reservedseat = reservedseat;
    }

    public String getVacantseat() {
        return vacantseat;
    }

    public void setVacantseat(String vacantseat) {
        this.vacantseat = vacantseat;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /*public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }*/
}
