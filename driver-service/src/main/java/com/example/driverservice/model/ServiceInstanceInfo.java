package com.example.driverservice.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Diagnostic DTO displaying instance execution details.
 */
public class ServiceInstanceInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String serviceName;
    private int port;
    private String instanceId;
    private String host;
    private String status;
    private LocalDateTime timestamp;
    private String message;

    public ServiceInstanceInfo() {
        this.timestamp = LocalDateTime.now();
    }

    public ServiceInstanceInfo(String serviceName, int port, String instanceId, String host, String status, String message) {
        this.serviceName = serviceName;
        this.port = port;
        this.instanceId = instanceId;
        this.host = host;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.message = message;
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
