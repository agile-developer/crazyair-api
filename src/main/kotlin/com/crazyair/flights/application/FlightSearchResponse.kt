package com.crazyair.flights.application

import com.crazyair.flights.domain.CabinClass
import com.crazyair.flights.domain.Flight
import java.math.BigDecimal
import java.time.LocalDateTime

data class FlightSearchResponse(
    val airline: String,
    val price: BigDecimal,
    val cabinClass: CabinClass,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime
) {
    companion object {
        fun fromFlight(flight: Flight) =
            with(flight) {
                FlightSearchResponse(
                    airline,
                    BigDecimal.valueOf(priceInPence).movePointLeft(2),
                    cabinClass,
                    departureAirportCode,
                    departureAirportCode,
                    departureDate,
                    arrivalDate
                )
            }
    }
}
