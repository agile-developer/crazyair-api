package com.crazyair.flights.domain

import com.crazyair.flights.application.FlightSearchRequest
import com.crazyair.flights.domain.SearchResult.Empty
import com.crazyair.flights.domain.SearchResult.UnsupportedAirport
import com.crazyair.flights.domain.SearchResult.Found
import com.crazyair.flights.infra.FlightRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FlightSearchServiceTest {

    private val flightRepository = mockk<FlightRepositoryImpl>()
    private val flightSearchService = FlightSearchServiceImpl(flightRepository)

    @Test
    fun `search result is 'Found' when repository returns results`() {
        // arrange
        val origin = "LHR"
        val destination = "DXB"
        val departureDate = LocalDate.of(2024, 9, 1)
        val returnDate = LocalDate.of(2024, 9, 10)
        val passengerCount = 4
        val searchRequest = FlightSearchRequest(origin, destination, departureDate, returnDate, passengerCount)
        val flight1 = Flight(1, "Emirates", 50000, CabinClass.E, origin, destination, departureDate.atStartOfDay(), returnDate.atStartOfDay(), 10)
        val flight2 = Flight(2, "British Airways", 60000, CabinClass.B, origin, destination, departureDate.atStartOfDay(), returnDate.atStartOfDay(), 20)
        every { flightRepository.findFlights(origin, destination, passengerCount) } returns listOf(flight1, flight2)

        // act
        val result= flightSearchService.searchFlights(searchRequest)

        // assert
        assertThat(result).isInstanceOf(Found::class.java)
        assertThat((result as Found).flights.size).isEqualTo(2)
    }

    @Test
    fun `search result is 'Empty' when repository returns zero results`() {
        // arrange
        val origin = "LHR"
        val destination = "DXB"
        val departureDate = LocalDate.of(2024, 9, 1)
        val returnDate = LocalDate.of(2024, 9, 10)
        val passengerCount = 4
        val searchRequest = FlightSearchRequest(origin, destination, departureDate, returnDate, passengerCount)
        every { flightRepository.findFlights(origin, destination, passengerCount) } returns emptyList()

        // act
        val result= flightSearchService.searchFlights(searchRequest)

        // assert
        assertThat(result).isInstanceOf(Empty::class.java)
    }

    @Test
    fun `search result is 'UnsupportedAirport' when origin airport code is unknown`() {
        // arrange
        val origin = "LGW"
        val destination = "DXB"
        val departureDate = LocalDate.of(2024, 9, 1)
        val returnDate = LocalDate.of(2024, 9, 10)
        val passengerCount = 4
        val searchRequest = FlightSearchRequest(origin, destination, departureDate, returnDate, passengerCount)
        every { flightRepository.findFlights(origin, destination, passengerCount) } returns emptyList()

        // act
        val result= flightSearchService.searchFlights(searchRequest)

        // assert
        assertThat(result).isInstanceOf(UnsupportedAirport::class.java)
        assertThat((result as UnsupportedAirport).message).isEqualTo("Origin: $origin is not a supported airport code")
    }
}