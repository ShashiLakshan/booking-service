# Booking Microservice

This is a Spring Boot 3.3.2 application that uses H2 as its database.
Booking can be saved with Booking Id, Event Id, User Name, Ticket Type, No of Tickets and Pay Amount. Also a booking can be cancelled.

Other related microservices:
* Event Microservice
* Payment Microservice
* Notification Microservice

#### Swagger URL: http://localhost:8082/swagger-ui/index.html
#### Postman Collection: https://github.com/ShashiLakshan/event-service/blob/main/postman/event-booking.postman_collection.json

## Prerequisites

- Java 22 or later
- Maven 3.8.1 or later
- Docker (for Kafka)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/ShashiLakshan/booking-service.git
cd booking-service
```

### Building the Project
```bash
mvn clean install
```
### Running the Application
```bash
mvn spring-boot:run
```
The application will start and be accessible at http://localhost:8082.

## REST APIs

### POST /api/v1/bookings
#### Request
```json
{
  "eventId": 1,
  "userName": "Saman",
  "noOfTickets": 4,
  "ticketType": "VIP"
}
```
#### Response
```json
{
  "bookingId": 1,
  "eventId": 1,
  "userName": "Saman",
  "ticketType": "VIP",
  "noOfTickets": 4,
  "totalAmt": 400.00
}
```

### GET /api/v1/bookings/{id}
#### Response
```json
{
  "bookingId": 1,
  "eventId": 1,
  "userName": "Saman",
  "ticketType": "VIP",
  "noOfTickets": 4,
  "totalAmt": 400.00
}
```


### DELETE /api/v1/bookings/{id?
#### Response
```json
200 OK
```
