# Sleuth Zipkin Demo

This project demonstrates distributed tracing in a microservices architecture using Spring Cloud Sleuth and Zipkin. It consists of three Spring Boot applications: a Producer service that manages employee data, a Mediator service that proxies requests to the Producer, and a Consumer service that proxies requests to the Mediator.

## Architecture

- **Employee Producer** (Port 8090): The data source service that exposes REST endpoints for CRUD operations on employee records. It stores data in-memory.
- **Employee Mediator** (Port 8095): An intermediary service that forwards all requests to the Producer using RestTemplate.
- **Employee Consumer** (Port 8096): The client-facing service that forwards requests to the Mediator.

Requests flow: Consumer → Mediator → Producer. Sleuth injects trace IDs into logs and HTTP headers for end-to-end tracing, and Zipkin collects and visualizes the traces.

## Prerequisites

- Java 8 or higher
- Maven 3.6+
- Zipkin server (for trace visualization)

## Running the Project

### 1. Start Zipkin Server

Download the latest Zipkin JAR from [zipkin.io](https://zipkin.io/) and run it:

```bash
java -jar zipkin-server-*.jar
```

Alternatively, use Docker:

```bash
docker run -d -p 9411:9411 openzipkin/zipkin
```

Zipkin UI will be available at `http://localhost:9411`.

### 2. Run the Services

Open three terminal windows in the project root directory (`D:\Repositories\Sleuth_Zipkin_Demo`).

- **Run Producer**:
  ```bash
  cd Employee_Producer_Sleuth_Zipkin
  mvn spring-boot:run
  ```
  Starts on port 8090.

- **Run Mediator**:
  ```bash
  cd Employee_Mediator_Sleuth_Zipkin
  mvn spring-boot:run
  ```
  Starts on port 8095.

- **Run Consumer**:
  ```bash
  cd Employee_Consumer_Sleuth_Zipkin
  mvn spring-boot:run
  ```
  Starts on port 8096.

Verify each service is running by checking the logs or hitting the welcome endpoint (e.g., `http://localhost:8090/` returns "Welcome").

### 3. Test the Application

Use a tool like Postman, curl, or your browser to make requests to the Consumer service (port 8096). The requests will propagate through Mediator to Producer.

#### Endpoints

All endpoints are mirrored across services. Test via Consumer for full tracing.

- `GET /emp/controller/getDetails`: Get all employees.
- `GET /emp/controller/getDetailsById/{id}`: Get employee by ID.
- `POST /emp/controller/addEmp`: Add a new employee (JSON body: `{"empoloyeeName":"Name","salary":10000,"departmentCode":100}`).
- `PUT /emp/controller/updateEmp`: Update an employee (JSON body with `employeeId`).
- `DELETE /emp/controller/deleteEmp/{id}`: Delete employee by ID.

#### Example Requests

- Get all employees:
  ```bash
  curl http://localhost:8096/emp/controller/getDetails
  ```
  Expected: JSON array of employees.

- Add an employee:
  ```bash
  curl -X POST http://localhost:8096/emp/controller/addEmp \
       -H "Content-Type: application/json" \
       -d '{"empoloyeeName":"Alice","salary":15000,"departmentCode":1004}'
  ```

- Update an employee:
  ```bash
  curl -X PUT http://localhost:8096/emp/controller/updateEmp \
       -H "Content-Type: application/json" \
       -d '{"empoloyeeName":"Jack Updated","employeeId":10001,"salary":13000,"departmentCode":1001}'
  ```

### 4. Verify Tracing

- Check logs in each service's `app.log` for trace IDs (e.g., `[consumer,abc123,def456]`). Trace IDs should match across services for the same request.
- In Zipkin UI (`http://localhost:9411`):
  - Click "Run Query" to list traces.
  - Select a trace to view the timeline: Consumer → Mediator → Producer, with spans for each HTTP call and timings.
- Sampling is set to 100%, so all requests are traced.

### Additional Notes

- If ports are in use, edit `application.properties` in each service to change `server.port`.
- Run `mvn test` in each service directory to execute unit tests.
- For production, configure Zipkin URL in `application.properties` if not using localhost:9411.
- This demo uses in-memory storage; data resets on restart.
