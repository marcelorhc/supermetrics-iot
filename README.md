# IoT Data Processing System

Hi, I really enjoyed working on this assignment. I hope you like the solution.

Since there was no contracts, my idea was a flow to create and manage the existing devices and a flow to save the
readings and query for the aggregations (as requested in the document).

For saving the readings I left an open endpoint and I manually map the devices we are accepting.

I also assume the amount of devices would not be that high, thats why I left a find all devices.

Performance: 
- I tested saving 600 readings per second and it was working fine
- The aggregation was around ~150ms with 1 million documents in mongo

Any further implementations/fix we can discuss in the next step.

To turn on the simulator:
POST : /simulator/on (you can find the CURL bellow)

# Tech stack

- Java 21
- Mongo DB
- Spring boot 3
- Rest Assured
- Test Containers
- Swagger
- Docker

# Running with Docker

The application can be run using Docker and Docker Compose:

```bash
# Generate the jar
mvn clean install

# Build and start the application with mongo
docker-compose up

# Stop the application
docker-compose down
```

The application will be available at http://localhost:8080 with Swagger UI at http://localhost:8080/swagger-ui.html


# Request examples:

## Simulator

TURN ON
```bash
curl -X POST "http://localhost:8080/api/simulator/on" \
  -u "user:password" \
  -H "Content-Type: application/json"
```

TURN OFF
```bash
curl -X POST "http://localhost:8080/api/simulator/off" \
  -u "user:password" \
  -H "Content-Type: application/json"
```

## Device Endpoints

### Get All Devices

Retrieves a list of all IoT devices.

```bash
curl -X GET "http://localhost:8080/api/devices" \
  -u "user:password" \
  -H "Content-Type: application/json"
```

### Get Device by ID

Retrieves a specific device by its ID.

```bash
curl -X GET "http://localhost:8080/api/devices/device-123" \
  -u "user:password" \
  -H "Content-Type: application/json"
```

### Create a New Device

Creates a new IoT device.

```bash
curl -X POST "http://localhost:8080/api/devices" \
  -u "user:password" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smart Thermostat",
    "brand": "SuperMetrics",
    "serialNumber": "SN-0012345678",
    "type": "TEMPERATURE_SENSOR",
    "zone": "Living Room",
    "active": true
  }'
```

### Update an Existing Device

Updates an existing IoT device by its ID.

```bash
curl -X PUT "http://localhost:8080/api/devices/device-123" \
  -u "user:password" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "device-123",
    "name": "Smart Thermostat Pro",
    "brand": "SuperMetrics",
    "serialNumber": "SN-0012345678",
    "type": "TEMPERATURE_SENSOR",
    "zone": "Living Room",
    "active": true
  }'
```

### Delete a Device

Deletes an IoT device by its ID.

```bash
curl -X DELETE "http://localhost:8080/api/devices/device-123" \
  -u "user:password"
```

## Reading Endpoints

### Get Aggregated Readings (All Parameters)

Retrieves aggregated readings with all available filters.

```bash
curl -X GET "http://localhost:8080/api/readings?category=HEALTH&deviceIds=apple-watch-123&deviceIds=fitbit-456&zone=Bedroom&startTime=2024-01-01T00:00:00&endTime=2024-01-02T23:59:59" \
  -u "user:password" \
  -H "Content-Type: application/json"
```

### Get Aggregated Readings (Required Parameters Only)

Retrieves aggregated readings with only required time parameters.

```bash
curl -X GET "http://localhost:8080/api/readings?startTime=2024-01-01T00:00:00&endTime=2024-01-02T23:59:59" \
  -u "user:password" \
  -H "Content-Type: application/json"
```

### Get Aggregated Readings (Filtered by Zone)

Retrieves aggregated readings filtered by device zone.

```bash
curl -X GET "http://localhost:8080/api/readings?zone=Bedroom&startTime=2024-01-01T00:00:00&endTime=2024-01-02T23:59:59" \
  -u "user:password" \
  -H "Content-Type: application/json"
```

### Get Aggregated Readings (Filtered by Device Category)

Retrieves aggregated readings filtered by device category.

```bash
curl -X GET "http://localhost:8080/api/readings?category=HEALTH&startTime=2024-01-01T00:00:00&endTime=2024-01-02T23:59:59" \
  -u "user:password" \
  -H "Content-Type: application/json"
```

### Save Apple Heart Rate Reading

Saves a new heart rate reading from an Apple device.

```bash
curl -X POST "http://localhost:8080/api/readings" \
  -u "device:device" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "apple-watch-123",
    "brand": "Apple",
    "collected_at": "2024-01-01T12:00:00Z",
    "bpm": 72,
    "activity": "Running"
  }'
```

## Notes

- Time parameters should be in ISO 8601 format (`YYYY-MM-DDTHH:mm:ss`)
- Multiple `deviceIds` can be specified by repeating the parameter

