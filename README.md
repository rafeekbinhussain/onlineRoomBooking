# Conference Room Booking API

This API allows users to book conference rooms for meetings and retrieve information about booked slots.

## Book a Conference Room

Endpoint: `POST /api/conference-room/book-room`

### Request

```bash
curl --location 'http://localhost:8080/api/conference-room/book-room' \
--header 'Content-Type: application/json' \
--data '{
    "noOfPerson": 3,
    "empId": "rafeek",
    "startTime": "09:00",
    "endTime": "10:00"
}'
```

### Response (Success)

```json
{
    "bookingStatus": true,
    "roomName": "Amaze",
    "capacity": 3,
    "noOfPerson": 3,
    "empId": "rafeek",
    "startTime": "09:00",
    "endTime": "10:00"
}
```

### Response (Error)

```json
{
    "status": 500,
    "message": "Error processing the request",
    "details": "Room already booked with mentioned time frame"
}
```

## Get All Booked Slots

Endpoint: `GET /api/conference-room/booked-slots`

### Request

```bash
curl --location 'http://localhost:8080/api/conference-room/booked-slots'
```

### Response

```json
[
    {
        "id": 1,
        "bookingStatus": true,
        "roomName": "Inspire",
        "noOfPerson": 9,
        "empId": "234",
        "startTime": "09:00",
        "endTime": "10:00",
        "bookingTimestamp": "2024-01-20T15:16:06.278812"
    },
    {
        "id": 2,
        "bookingStatus": true,
        "roomName": "Amaze",
        "noOfPerson": 3,
        "empId": "rafeek",
        "startTime": "09:00",
        "endTime": "10:00",
        "bookingTimestamp": "2024-01-20T15:16:20.477973"
    }
]
```

### Get Booked History by Room Name

Retrieve the booking history for a specific conference room by providing the room name.

```bash
curl --location 'http://localhost:8080/api/conference-room/booked-history/{roomName}'
```

Replace `{roomName}` with the name of the conference room you want to retrieve booking history for.

**Example:**

```bash
curl --location 'http://localhost:8080/api/conference-room/booked-history/strive'
```

**Success Response:**

```json
[
    {
        "id": 1,
        "bookingStatus": true,
        "roomName": "Strive",
        "noOfPerson": 15,
        "empId": "user123",
        "startTime": "09:00",
        "endTime": "10:30",
        "bookingTimestamp": "2024-01-20T15:16:06.278812"
    },
    {
        "id": 2,
        "bookingStatus": true,
        "roomName": "Strive",
        "noOfPerson": 10,
        "empId": "user456",
        "startTime": "11:00",
        "endTime": "12:30",
        "bookingTimestamp": "2024-01-20T15:16:20.477973"
    }
]
```

**Error Response:**

```json
{
    "status": 404,
    "message": "Room not found",
    "details": "No booking history found for the specified room."
}
```

Make sure to replace `http://localhost:8080` with the actual base URL of your application.

## Environment

- Java 17
- Maven
- Spring Boot
- In-memory Database

### Running the Application

Ensure you have Java 17 and Maven installed. Clone the repository and run the following commands:

```bash
mvn clean install
mvn spring-boot:run
```

The application will be accessible at `http://localhost:8080`.