# Smart Lift Dispatch System

## 1. Problem Statement
Managing efficient lift dispatch in a building with 16 floors and 3 basements. The building has two lift sections—odd and even floors—with shared access to basements and ground floor. The system optimizes lift assignment by dispatching the nearest available lift, handles emergency overrides, and supports maintenance modes to improve reliability and safety.

## 2. System Design
- **Architecture:** Spring Boot microservice exposing REST APIs.
- **Components:**
  - `LiftControllerAPI`: Handles REST endpoints for lift requests.
  - `DispatchManager`: Contains core logic to allocate lifts based on proximity and availability.
  - `Lift` model: Represents each lift’s state, direction, capacity, and queue of destinations.
  - Configuration files define building layout and lift capacities.
- **Flow:** Client → REST API → DispatchManager → Lift → Response.

*(Add a diagram here if possible: class diagram or sequence flow)*

## 3. Features
- Separation of odd and even floor lift sections with shared basement/ground floor access.
- Lift capacity tracking and maintenance mode support.
- Emergency priority override to bypass normal dispatch logic.
- Dispatch based on nearest available lift to minimize wait times.
- Comprehensive unit tests for core functionalities.

## 4. Project Structure
/src
> main
>> java/com/lift/config # Configuration classes
>> java/com/lift/controller # REST Controllers
>> java/com/lift/model # Data models (Lift, LiftRequest, etc.)
>> java/com/lift/service # Business logic (DispatchManager, etc.)
> test
>> java/com/lift # Unit tests

## 5. Running the Project

### Prerequisites:
- Java 17+
- Maven 3.6+
- IDE like IntelliJ IDEA (optional)

### Build & Run:
- mvn clean install
- mvn spring-boot:run
- The service will start on http://localhost:8080.

## 6. API Endpoints
- POST /api/lifts/request
- Request a lift to a specific floor.

### Request body:
    {
      "floor": 5,
      "emergency": false
    }

### Response:
    {
      "id": "O1",
      "section": "odd",
      "currentFloor": 0,
      "direction": "UP",
      "inMaintenance": false,
      "capacity": 8,
      "currentLoad": 0,
      "destinationQueue": [5]
    }

## 7. Future Improvements
- Real-time GUI dashboard to visualize lift statuses.
- External API integration with building management systems.
- Dynamic lift capacity allocation based on usage analytics.
- Predictive dispatch using machine learning models on historical data.
- Energy-saving modes during off-peak hours.

