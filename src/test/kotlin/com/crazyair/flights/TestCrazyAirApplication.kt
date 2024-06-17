package com.crazyair.flights

import org.springframework.boot.fromApplication


fun main(args: Array<String>) {
	fromApplication<CrazyAirApplication>().with(TestContainersConfiguration::class.java).run(*args)
}
