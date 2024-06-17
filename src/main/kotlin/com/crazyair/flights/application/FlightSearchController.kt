package com.crazyair.flights.application

import com.crazyair.flights.application.FlightSearchResponse.Companion.fromFlight
import com.crazyair.flights.domain.FlightSearchService
import com.crazyair.flights.domain.SearchResult
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/crazyair/flights")
class FlightSearchController(
    private val flightSearchService: FlightSearchService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun searchFlights(
        @RequestBody flightSearchRequest: FlightSearchRequest
    ): ResponseEntity<*> {

        flightSearchRequest.validate()
        return when(val searchResult = flightSearchService.searchFlights(flightSearchRequest)) {
            is SearchResult.Found -> ResponseEntity.ok(searchResult.flights.map { fromFlight(it) })
            is SearchResult.Empty -> ResponseEntity.ok(searchResult.NO_RESULTS)
            is SearchResult.UnsupportedAirport -> ResponseEntity.badRequest().body(searchResult.message)
        }
    }
}
