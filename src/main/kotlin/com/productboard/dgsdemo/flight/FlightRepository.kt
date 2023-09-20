package com.productboard.dgsdemo.flight

import com.productboard.dgddemo.jooq.Tables.FLIGHTS
import com.productboard.dgsdemo.Page
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class FlightRepository(private val jooqContext: DSLContext) {
    fun list(limit: Int, after: String? = null, filter: FlightsFilter? = null): Page<Flight> =
        jooqContext
            .select(DSL.asterisk())
            .from(FLIGHTS)
            .where(filter.toCondition())
            .orderBy(FLIGHTS.START_TIME.asc())
            .limit(limit + 1)
            .fetch { serializeFlight(it) }
            .let { Page(nodes = it.take(limit), hasNextPage = it.size > limit, hasPreviousPage = after != null) }

    fun count(filter: FlightsFilter? = null): Int =
        jooqContext.selectCount().from(FLIGHTS).where(filter.toCondition()).fetchSingle(0, Int::class.java) ?: 0

    private fun serializeFlight(record: Record): Flight =
        Flight(
            id = record[FLIGHTS.ID],
            startTime = record[FLIGHTS.START_TIME],
            landingTime = record[FLIGHTS.LANDING_TIME],
            planeSign = record[FLIGHTS.PLANE]
        )
}

private fun FlightsFilter?.toCondition(): Condition =
    if (this == null) {
        DSL.noCondition()
    } else {
        if (plane != null) {
            FLIGHTS.PLANE.eq(plane)
        } else {
            DSL.noCondition()
        }
    }
