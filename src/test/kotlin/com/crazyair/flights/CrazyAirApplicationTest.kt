package com.crazyair.flights

import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@Import(TestContainersConfiguration::class)
@SpringBootTest
@AutoConfigureMockMvc
class CrazyAirApplicationTest {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Autowired
	private lateinit var jdbcClient: JdbcClient

//	@Test
//	fun contextLoads() {
//	}

	@BeforeEach
	fun init() {
		jdbcClient.sql("DELETE FROM flights").update()
	}

	@Test
	fun `searching for flights for known airports and available dates returns 200 'OK' and non-zero results`() {
		// arrange
		val flight1 = """
			INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
			VALUES ('Emirates', 'E', 'LHR', 'DXB', '2024-09-01 08:00:00', '2024-09-10 08:00:00', 55000, 51)
		""".trimIndent()
		val flight2 = """
			INSERT INTO flights (airline, cabin_class, departure_airport_code, destination_airport_code, departure_date, arrival_date, price_in_pence, seats_available)
			VALUES ('British Airways', 'B', 'LHR', 'DXB', '2024-09-01 08:00:00', '2024-09-10 08:00:00', 65000, 41)
		""".trimIndent()
		jdbcClient.sql(flight1).update()
		jdbcClient.sql(flight2).update()
		val searchRequest = """
			{
				"origin": "LHR",
				"destination": "DXB",
				"departureDate": "2024-09-01",
				"returnDate": "2024-09-10",
				"passengerCount": 4
			}
		""".trimIndent()

		// act
		val result = mockMvc.perform(
			post("/crazyair/flights")
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(searchRequest)
		).andReturn()

		// assert
		assertThat(result.response.status).isEqualTo(200)
		val responseBodyJson = result.response.contentAsString
		val searchResultSize = JsonPath.read<Int>(responseBodyJson, "$.length()")
		assertThat(searchResultSize).isEqualTo(2)
	}

	@Test
	fun `searching for flights for known airports and unavailable dates returns 200 'OK' and zero results`() {
		// arrange
		val searchRequest = """
			{
				"origin": "LHR",
				"destination": "DXB",
				"departureDate": "2024-10-01",
				"returnDate": "2024-10-10",
				"passengerCount": 4
			}
		""".trimIndent()

		// act
		val result = mockMvc.perform(
			post("/crazyair/flights")
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(searchRequest)
		).andReturn()

		// assert
		assertThat(result.response.status).isEqualTo(200)
		assertThat(result.response.contentAsString).isEqualTo("Search returned no results")
	}

	@Test
	fun `searching for flights for unknown airports returns 400 'Bad Request'`() {
		// arrange
		val searchRequest = """
			{
				"origin": "LGW",
				"destination": "BER",
				"departureDate": "2024-07-01",
				"returnDate": "2024-07-10",
				"passengerCount": 4
			}
		""".trimIndent()

		// act
		val result = mockMvc.perform(
			post("/crazyair/flights")
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(searchRequest)
		).andReturn()

		// assert
		assertThat(result.response.status).isEqualTo(400)
		assertThat(result.response.contentAsString).endsWith("is not a supported airport code")
	}

	@Test
	fun `searching for flights with invalid request data returns 400 'Bad Request'`() {
		// arrange
		val searchRequest = """
			{
				"origin": "LHRA",
				"destination": "DXBA",
				"departureDate": "2024-05-01",
				"returnDate": "2024-04-10",
				"passengerCount": -1
			}
		""".trimIndent()

		// act
		val result = mockMvc.perform(
			post("/crazyair/flights")
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.content(searchRequest)
		).andReturn()

		// assert
		assertThat(result.response.status).isEqualTo(400)
		println(result.response.contentAsString.split("\n"))
		assertThat(result.response.contentAsString.split("\n"))
			.contains(
				"Origin is invalid",
				"Destination is invalid",
				"Departure date cannot be in the past",
				"Return date cannot be in the past",
				"Return date must be later than departure date",
				"Passenger count must be greater than zero")
	}
}
