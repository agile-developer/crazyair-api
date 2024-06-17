package com.crazyair.flights

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CrazyAirApplication

fun main(args: Array<String>) {
	runApplication<CrazyAirApplication>(*args)
}
