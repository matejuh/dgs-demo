package com.productboard.dgsdemo.flight

import com.productboard.dgsdemo.DgsDemoApplicationTests
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class FlightRepositoryTest : DgsDemoApplicationTests() {
    @Autowired private lateinit var flightRepository: FlightRepository

    @Test
    fun `Should list`() {
        val flights = flightRepository.list(10, null)
        assertThat(flights.nodes).hasSize(10)
        assertThat(flights.hasPreviousPage).isFalse()
        assertThat(flights.hasNextPage).isTrue()
    }

    @Test
    fun `Should list with filter`() {
        assertThat(
                flightRepository.list(100, null, FlightsFilter(plane = "OK-6400")).nodes.map { it.planeSign }.distinct()
            )
            .containsExactly("OK-6400")
    }

    @Test
    fun `Should get count with filter`() {
        assertThat(flightRepository.count(FlightsFilter(plane = "OK-6400"))).isEqualTo(4)
    }

    @Test
    fun `Should get by ids`() {
        val ids = setOf(1, 2, 3)
        assertThat(flightRepository.getByIds(ids)).hasSize(ids.size)
    }
}
