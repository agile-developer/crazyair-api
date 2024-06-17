package com.crazyair.flights.domain

import com.crazyair.flights.application.FlightSearchRequest
import com.crazyair.flights.infra.FlightRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FlightSearchServiceImpl(
    private val flightRepository: FlightRepository
) : FlightSearchService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun searchFlights(searchRequest: FlightSearchRequest): SearchResult {
        logger.info("Searching for flights")
        val result = runCatching {
            checkAirport(searchRequest.origin, "Origin")
            checkAirport(searchRequest.destination, "Destination")
            flightRepository.findFlights(
                searchRequest.origin, searchRequest.destination, searchRequest.passengerCount
            ).filter {
                it.departureDate.toLocalDate().isEqual(searchRequest.departureDate) &&
                it.arrivalDate.toLocalDate().isEqual(searchRequest.returnDate)
            }
        }.fold(
            onSuccess = {
                if (it.isEmpty()) {
                    logger.info("Search returned zero results")
                    SearchResult.Empty
                } else {
                    SearchResult.Found(it)
                }
            },
            onFailure = {
                logger.error("Error encountered searching for flights: ${it.message!!}")
                SearchResult.UnsupportedAirport(it.message!!)
            }
        )

        return result
    }

    private fun checkAirport(airportCode: String, journeyLeg: String): Airport {
        return runCatching {
            Airport.valueOf(airportCode)
        }.getOrElse {
            throw IllegalArgumentException("$journeyLeg: $airportCode is not a supported airport code")
        }
    }
}