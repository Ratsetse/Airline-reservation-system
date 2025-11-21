package com.example.models;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;            // instead of customerId
    private String name;
    private String address;
    private String meterNumber;
    private double usage;
    private double lastBill;      // instead of billAmount

    public Customer() {}

    public Customer(String id, String name, String address, String meterNumber, double usage, double lastBill) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.meterNumber = meterNumber;
        this.usage = usage;
        this.lastBill = lastBill;
    }

    // ✅ Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getMeterNumber() { return meterNumber; }
    public double getUsage() { return usage; }
    public double getLastBill() { return lastBill; }

    // ✅ Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public void setUsage(double usage) { this.usage = usage; }
    public void setLastBill(double lastBill) { this.lastBill = lastBill; }

    @Override
    public String toString() {
        return id + " - " + name + " (M" + lastBill + ")";
    }
}
