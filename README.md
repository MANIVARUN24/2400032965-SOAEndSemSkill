# Spring Boot Microservices - Round Robin Load Balancing

A complete Spring Boot and Spring Cloud microservices project demonstrating **Round Robin Load Balancing** with **Eureka Service Discovery** and **Spring Cloud LoadBalancer**. Built with Java 21 and Spring Boot 3.3.4.

---

## 🏛️ Architecture Overview

```
                      +-----------------------------+
                      |   Eureka Discovery Server   |
                      |        (Port: 8761)         |
                      +--------------+--------------+
                                     ^
               Registers & Heartbeats| Discovers Instances
                                     v
                        +-------------------------+
                        |       API Gateway       |
                        |      (Port: 8080)       |
                        |  Spring Cloud Gateway   |
                        | + RoundRobinLoadBalancer|
                        +------------+------------+
                                     |
               Round-Robin Request 1 | Round-Robin Request 2
               (50% traffic)         | (50% traffic)
                                     v
                  +------------------+------------------+
                  |                                     |
                  v                                     v
     +--------------------------+          +--------------------------+
     |      driver-service      |          |      driver-service      |
     |        Instance 1        |          |        Instance 2        |
     |       (Port: 8081)       |          |       (Port: 8082)       |
     +--------------------------+          +--------------------------+
```

---

## 📦 Microservices

| Service Name | Port | Description | Technology Stack |
| :--- | :--- | :--- | :--- |
| **`eureka-server`** | `8761` | Central Service Discovery Registry | Spring Cloud Netflix Eureka Server, Actuator |
| **`driver-service`** (Instance 1) | `8081` | Driver management microservice (instance 1) | Spring Boot Web, Eureka Client, Actuator |
| **`driver-service`** (Instance 2) | `8082` | Driver management microservice (instance 2) | Spring Boot Web, Eureka Client, Actuator |
| **`api-gateway`** | `8080` | Unified edge gateway with Round-Robin Load Balancing | Spring Cloud Gateway (WebFlux), Spring Cloud LoadBalancer, Eureka Client |

---

## 🚀 Key Features

1. **Service Registration & Discovery**: Both instances of `driver-service` and `api-gateway` register with `eureka-server`.
2. **Spring Cloud LoadBalancer (Round-Robin)**: Gateway routes requests using the `lb://driver-service` scheme. Spring Cloud LoadBalancer distributes requests sequentially in round-robin fashion between ports 8081 and 8082.
3. **Observability & Logging**:
   - Every response contains `port`, `serviceName`, and `instanceId` metadata.
   - `RoundRobinLoggingFilter` logs incoming gateway routes and appends diagnostic response headers:
     - `X-Gateway-Routed-To: http://localhost:8081/drivers` or `http://localhost:8082/drivers`
     - `X-Gateway-LoadBalancer: RoundRobinLoadBalancer`
     - `X-Handled-By-Port: 8081` / `8082`
4. **Spring Tool Suite (STS) & Maven Multi-Module Ready**: Complete standard Maven directory structure compatible with STS 4, Eclipse, IntelliJ IDEA, and CLI.

---

## 🛠️ Prerequisites

- **Java JDK 21** or higher
- **Maven 3.8+** (or use the IDE's embedded Maven)
- **Spring Tool Suite 4 (STS)** or IntelliJ IDEA / Eclipse

---

## 🖥️ Running in Spring Tool Suite (STS)

### 1. Import the Project into STS
1. Open **Spring Tool Suite**.
2. Click **File** ➔ **Import...** ➔ **Maven** ➔ **Existing Maven Projects**.
3. Browse to the root directory `ROUND-ROBIN-LOAD-BALANCER`.
4. Select all four pom.xml entries:
   - `round-robin-load-balancer` (root)
   - `eureka-server`
   - `driver-service`
   - `api-gateway`
5. Click **Finish**.

### 2. Start Microservices in Order

#### Step 1: Start Eureka Server (Port 8761)
- In the **Boot Dashboard** or **Project Explorer**, right-click `eureka-server` ➔ **Run As** ➔ **Spring Boot App** (or run `EurekaServerApplication.java`).
- Verify in browser: [http://localhost:8761](http://localhost:8761)

#### Step 2: Start Driver Service Instance 1 (Port 8081)
- Right-click `driver-service` ➔ **Run As** ➔ **Spring Boot App** (or run `DriverServiceApplication.java`).
- It defaults to port `8081`.

#### Step 3: Start Driver Service Instance 2 (Port 8082)
- In STS, go to **Run** ➔ **Run Configurations...**
- Under **Spring Boot App**, right-click `DriverServiceApplication` ➔ **Duplicate**.
- Name it: `DriverServiceApplication - Port 8082`.
- In the **Arguments** tab, add to **VM arguments**:
  ```text
  -Dserver.port=8082
  ```
  *(Or in Spring Boot tab, set Active Profile to `instance2`)*.
- Click **Run**.
- Both instances (`8081` and `8082`) will register under the `DRIVER-SERVICE` name in Eureka!

#### Step 4: Start API Gateway (Port 8080)
- Right-click `api-gateway` ➔ **Run As** ➔ **Spring Boot App** (or run `ApiGatewayApplication.java`).
- Runs on port `8080`.

---

## 💻 Running via Command Line (Terminal)

### 1. Build the Entire Project
```bash
mvn clean package -DskipTests
```

### 2. Launch Services in Separate Terminals

**Terminal 1: Eureka Server**
```bash
cd eureka-server
java -jar target/eureka-server-1.0.0-SNAPSHOT.jar
```

**Terminal 2: Driver Service (Instance 1 - Port 8081)**
```bash
cd driver-service
java -jar target/driver-service-1.0.0-SNAPSHOT.jar --server.port=8081
```

**Terminal 3: Driver Service (Instance 2 - Port 8082)**
```bash
cd driver-service
java -jar target/driver-service-1.0.0-SNAPSHOT.jar --server.port=8082
```

**Terminal 4: API Gateway (Port 8080)**
```bash
cd api-gateway
java -jar target/api-gateway-1.0.0-SNAPSHOT.jar
```

---

## 🧪 Testing Round-Robin Load Balancing

Send repeated requests through the **API Gateway** (`http://localhost:8080/drivers`):

### 1. PowerShell / Terminal Test:
```powershell
# Send 4 requests through API Gateway to observe alternating ports
1..4 | ForEach-Object {
    $res = Invoke-RestMethod -Uri "http://localhost:8080/drivers/info"
    [PSCustomObject]@{
        Request     = $_
        Port        = $res.port
        Service     = $res.serviceName
        InstanceId  = $res.instanceId
        Message     = $res.message
    }
}
```

### 2. cURL Test:
```bash
curl -i http://localhost:8080/drivers/info
curl -i http://localhost:8080/drivers/info
```

### 3. Expected Round-Robin Distribution Output:
```text
Request 1 -> Port: 8081 (Message: Hello from Driver Service instance active on port 8081)
Request 2 -> Port: 8082 (Message: Hello from Driver Service instance active on port 8082)
Request 3 -> Port: 8081 (Message: Hello from Driver Service instance active on port 8081)
Request 4 -> Port: 8082 (Message: Hello from Driver Service instance active on port 8082)
```

---

## 📡 REST API Reference

All requests can be accessed directly or routed via API Gateway (`http://localhost:8080`):

| Method | Endpoint | Description | Sample Gateway URL |
| :--- | :--- | :--- | :--- |
| `GET` | `/drivers` | Fetch all drivers | `http://localhost:8080/drivers` |
| `GET` | `/drivers/{id}` | Fetch driver by ID | `http://localhost:8080/drivers/1` |
| `GET` | `/drivers/info` | Diagnostic endpoint showing instance port | `http://localhost:8080/drivers/info` |
| `POST` | `/drivers` | Add a new driver | `http://localhost:8080/drivers` |
| `PUT` | `/drivers/{id}` | Update existing driver | `http://localhost:8080/drivers/1` |
| `DELETE` | `/drivers/{id}` | Delete a driver | `http://localhost:8080/drivers/1` |

### Sample POST Request Body:
```json
{
  "name": "Jordan Reed",
  "licenseNumber": "DL-772819",
  "vehicleType": "Sedan",
  "vehiclePlate": "KA-06-KL-9988",
  "rating": 4.95,
  "status": "AVAILABLE",
  "phoneNumber": "+1-555-0106"
}
```

---

## 🔍 Eureka Dashboard
Open your web browser and navigate to:
**`http://localhost:8761`**

You will see:
- `API-GATEWAY` (1 instance on port 8080)
- `DRIVER-SERVICE` (2 instances: ports 8081 and 8082)
