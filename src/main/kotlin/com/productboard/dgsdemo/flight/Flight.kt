package com.productboard.dgsdemo.flight

import com.productboard.dgsdemo.plane.PlaneSign
import java.time.LocalDateTime

data class Flight(
    val id: FlightId,
    val startTime: LocalDateTime,
    val landingTime: LocalDateTime,
    val planeSign: PlaneSign
)

typealias FlightId = Int

data class FlightsFilter(val plane: PlaneSign? = null)
