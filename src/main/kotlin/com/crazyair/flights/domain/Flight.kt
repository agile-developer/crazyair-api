package com.crazyair.flights.domain

import java.time.LocalDateTime

data class Flight(
    val id: Int,
    val airline: String,
    val priceInPence: Long,
    val cabinClass: CabinClass,
    val departureAirportCode: Airport,
    val destinationAirportCode: Airport,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime,
    val seatsAvailable: Int
)

enum class CabinClass(val description: String) {
    E("Economy"),
    B("Business")
}
