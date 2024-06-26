package com.crazyair.flights.application

import java.time.LocalDate

data class FlightSearchRequest(
    val origin: String,
    val destination: String,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val passengerCount: Int,
)

fun FlightSearchRequest.validate() {
    val validationErrors = mutableListOf<String>()
    if (origin.isBlank() || origin.length != 3) {
        validationErrors.add("Origin is invalid")
    }
    if (destination.isBlank() || destination.length != 3) {
        validationErrors.add("Destination is invalid")
    }
    if (departureDate.isBefore(LocalDate.now())) {
        validationErrors.add("Departure date cannot be in the past")
    }
    if (returnDate.isBefore(LocalDate.now())) {
        validationErrors.add("Return date cannot be in the past")
    }
    if (returnDate.isBefore(departureDate)) {
        validationErrors.add("Return date must be later than departure date")
    }
    if (passengerCount < 1) {
        validationErrors.add("Passenger count must be greater than zero")
    }

    if (validationErrors.isNotEmpty()) throw InvalidRequestException(validationErrors)
}
