package com.example.cms.bean;

import java.util.Date;

public class PassengerBean {
    private String flight;

    private Date date;

    private String from;

    private String to;

    private String passengerId;

    private String passengerName;

    private byte[] passengerFace;

    private String faceFeature;

    private String featureWithMask;

    private String flightClass;

    private String seat;

    private short gate;

    @Override
    public String toString() {
        return "PassengerBean{" +
                "flight='" + flight + '\'' +
                ", date=" + date +
                ", passengerId='" + passengerId + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", seat='" + seat + '\'' +
                '}';
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public byte[] getPassengerFace() {
        return passengerFace;
    }

    public void setPassengerFace(byte[] passengerFace) {
        this.passengerFace = passengerFace;
    }

    public String getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(String faceFeature) {
        this.faceFeature = faceFeature;
    }

    public String getFeatureWithMask() {
        return featureWithMask;
    }

    public void setFeatureWithMask(String featureWithMask) {
        this.featureWithMask = featureWithMask;
    }

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public short getGate() {
        return gate;
    }

    public void setGate(short gate) {
        this.gate = gate;
    }

}
