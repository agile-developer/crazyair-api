package com.crazyair.flights.application

import com.crazyair.flights.domain.Airport
import com.crazyair.flights.domain.CabinClass
import com.crazyair.flights.domain.Flight
import java.math.BigDecimal
import java.time.LocalDateTime

data class FlightSearchResponse(
    val airline: String,
    val price: BigDecimal,
    val cabinClass: CabinClass,
    val departureAirportCode: Airport,
    val destinationAirportCode: Airport,
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
                    destinationAirportCode,
                    departureDate,
                    arrivalDate
                )
            }
    }
}
