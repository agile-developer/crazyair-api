# CrazyAir API

### Summary
This is simple service that provides a single endpoint to search for flights. The service uses a Postgres database to store and retrieve flight data. There are currently no endpoints to add flight data, so flight data is populated using Liquibase migration scripts.

### Running the application
This project is implemented in Kotlin (`1.9.24`) and compiled for Java 21. Please ensure the target machine has Java 21 installed, and `java` is available on the `PATH`. It also uses Postgres for data, so a Postgres server must be available on or accessible from the local machine. The simplest way is to run Postgres as a Docker container. You'll need Docker Desktop, Podman, Rancher or some other OCI compliant runtime that can run container images.

For Docker, I've used the following command:
```shell
docker run -d \
--name crazyair-api \
-p 5433:5432 \
-e POSTGRES_DB=crazyair-db \
-e POSTGRES_USER=crazyair-user \
-e POSTGRES_PASSWORD=this1isNotGood! \
postgres:latest
```
**Important**: The database must be available before trying to run the application. The Postgres port `5432` is mapped to local port `5433` because we want to run multiple Postgres servers.

Once you've cloned the repository, or unzipped the source directory, change to the `crazyair-api` folder and run the following command, to build sources:
```shell
./gradlew clean build
```
To start the application, run:
```shell
java -jar build/libs/crazyair-api-0.0.1-SNAPSHOT.jar
```
This should start the application listening on port `8081` of your local machine. To test the endpoint use the following `curl` command:
```shell
curl -v \
-H 'Content-Type: application/json' \
-d '{"origin":"LHR", "destination":"DXB", "departureDate":"2024-07-01", "returnDate":"2024-07-10", "passengerCount":3}' \
http://localhost:8081/crazyair/flights
```
This should return a response like this:
```json
[
  {
    "airline": "Emirates",
    "price": 550.00,
    "cabinClass": "E",
    "departureAirportCode": "LHR",
    "destinationAirportCode": "LHR",
    "departureDate": "2024-07-01T08:00:00",
    "arrivalDate": "2024-07-10T08:00:00"
  },
  {
    "airline": "British Airways",
    "price": 650.00,
    "cabinClass": "B",
    "departureAirportCode": "LHR",
    "destinationAirportCode": "LHR",
    "departureDate": "2024-07-01T08:00:00",
    "arrivalDate": "2024-07-10T08:00:00"
  },
  {
    "airline": "Turkish Airlines",
    "price": 450.00,
    "cabinClass": "E",
    "departureAirportCode": "LHR",
    "destinationAirportCode": "LHR",
    "departureDate": "2024-07-01T08:00:00",
    "arrivalDate": "2024-07-10T08:00:00"
  },
  {
    "airline": "Lufthansa",
    "price": 750.00,
    "cabinClass": "B",
    "departureAirportCode": "LHR",
    "destinationAirportCode": "LHR",
    "departureDate": "2024-07-01T08:00:00",
    "arrivalDate": "2024-07-10T08:00:00"
  }
]
```
