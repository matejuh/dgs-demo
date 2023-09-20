package com.productboard.dgsdemo.flight

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.productboard.dgsdemo.dgs.types.Flight
import com.productboard.dgsdemo.dgs.types.FlightConnection
import com.productboard.dgsdemo.dgs.types.FlightEdge
import com.productboard.dgsdemo.dgs.types.FlightsFilter as GraphqlFlightsFilter
import com.productboard.dgsdemo.dgs.types.PageInfo
import com.productboard.dgsdemo.flight.Flight as DtoFlight
import com.productboard.dgsdemo.flight.FlightsFilter as DtoFlightsFilter
import graphql.execution.DataFetcherResult

@DgsComponent
class FlightFetcher(private val flightRepository: FlightRepository) {
    @DgsQuery
    fun listFlights(
        @InputArgument first: Int,
        @InputArgument after: String?,
        @InputArgument filter: GraphqlFlightsFilter?
    ): DataFetcherResult<FlightConnection> =
        flightRepository
            .list(first, after, filter.toDto())
            .let {
                FlightConnection(
                    edges =
                        it.nodes.map { flight ->
                            FlightEdge(node = flight.toGraphql(), cursor = flight.startTime.toString())
                        },
                    pageInfo =
                        PageInfo(
                            hasNextPage = it.hasNextPage,
                            hasPreviousPage = it.hasPreviousPage,
                            startCursor = it.nodes.firstOrNull()?.id.toString(),
                            endCursor = it.nodes.lastOrNull()?.id.toString()
                        )
                )
            }
            .let { DataFetcherResult.Builder<FlightConnection>().data(it).localContext(filter).build() }
}

private fun DtoFlight.toGraphql(): Flight =
    Flight(id = id.toString(), startTime = startTime, landingTime = landingTime, planeSign = planeSign)

private fun GraphqlFlightsFilter?.toDto(): DtoFlightsFilter? = this?.let { DtoFlightsFilter(plane = it.plane) }
