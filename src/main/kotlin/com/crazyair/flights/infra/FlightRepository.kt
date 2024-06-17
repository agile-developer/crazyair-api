package com.crazyair.flights.infra

import com.crazyair.flights.domain.Flight

interface FlightRepository {
    fun findFlights(origin: String, destination: String, passengers: Int): List<Flight>
}