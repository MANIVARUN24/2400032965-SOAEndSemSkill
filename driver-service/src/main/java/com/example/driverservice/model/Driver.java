package com.example.driverservice.model;

import java.io.Serializable;

/**
 * Driver entity representing driver details in the system.
 */
public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String licenseNumber;
    private String vehicleType;
    private String vehiclePlate;
    private Double rating;
    private String status;
    private String phoneNumber;

    public Driver() {
    }

    public Driver(Long id, String name, String licenseNumber, String vehicleType,
                  String vehiclePlate, Double rating, String status, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
        this.rating = rating;
        this.status = status;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", vehiclePlate='" + vehiclePlate + '\'' +
                ", rating=" + rating +
                ", status='" + status + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
