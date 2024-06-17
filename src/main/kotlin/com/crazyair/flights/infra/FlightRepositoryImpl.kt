package com.crazyair.flights.infra

import com.crazyair.flights.domain.CabinClass
import com.crazyair.flights.domain.Flight
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository

@Repository
class FlightRepositoryImpl(
    private val jdbcClient: JdbcClient
) : FlightRepository {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun findFlights(origin: String, destination: String, passengers: Int): List<Flight> {
        logger.info("Finding flights between $origin and $destination for $passengers passengers")

        val query = """
            SELECT * FROM flights f
            WHERE f.departure_airport_code = ?
            AND f.destination_airport_code = ?
            AND f.seats_available >= ?
        """.trimIndent()
        return jdbcClient.sql(query)
            .params(origin, destination, passengers)
            .query { rs, _ ->
                Flight(
                    rs.getInt("id"),
                    rs.getString("airline"),
                    rs.getLong("price_in_pence"),
                    CabinClass.valueOf(rs.getString("cabin_class")),
                    rs.getString("departure_airport_code"),
                    rs.getString("destination_airport_code"),
                    rs.getTimestamp("departure_date").toLocalDateTime(),
                    rs.getTimestamp("arrival_date").toLocalDateTime(),
                    rs.getInt("seats_available")
                )
            }.list()
    }
}
