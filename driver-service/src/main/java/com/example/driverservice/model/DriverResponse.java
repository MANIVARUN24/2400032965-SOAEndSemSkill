package com.example.driverservice.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Standard API response wrapper including metadata about which
 * microservice instance and port served the request.
 * Useful for verifying Round-Robin Load Balancing.
 */
public class DriverResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serviceName;
    private int port;
    private String instanceId;
    private LocalDateTime timestamp;
    private String message;
    private T data;

    public DriverResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public DriverResponse(String serviceName, int port, String instanceId, String message, T data) {
        this.serviceName = serviceName;
        this.port = port;
        this.instanceId = instanceId;
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
