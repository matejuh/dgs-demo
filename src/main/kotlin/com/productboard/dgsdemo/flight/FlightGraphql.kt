package com.productboard.dgsdemo.flight

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsDataLoader
import com.netflix.graphql.dgs.DgsEntityFetcher
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import com.productboard.dgsdemo.dgs.DgsConstants
import com.productboard.dgsdemo.dgs.types.Flight
import com.productboard.dgsdemo.dgs.types.FlightConnection
import com.productboard.dgsdemo.dgs.types.FlightEdge
import com.productboard.dgsdemo.dgs.types.FlightsFilter as GraphqlFlightsFilter
import com.productboard.dgsdemo.dgs.types.PageInfo
import com.productboard.dgsdemo.flight.Flight as DtoFlight
import com.productboard.dgsdemo.flight.FlightsFilter as DtoFlightsFilter
import graphql.execution.DataFetcherResult
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.Executor
import org.dataloader.MappedBatchLoader

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
                    edges = {
                        it.nodes.map { flight ->
                            FlightEdge.Builder()
                                .withNode(flight.toGraphql())
                                .withCursor(flight.startTime.toString())
                                .build()
                        }
                    },
                    pageInfo = {
                        PageInfo.Builder()
                            .withHasNextPage(it.hasNextPage)
                            .withHasPreviousPage(it.hasPreviousPage)
                            .withStartCursor(it.nodes.firstOrNull()?.id.toString())
                            .withEndCursor(it.nodes.lastOrNull()?.id.toString())
                            .build()
                    }
                )
            }
            .let { DataFetcherResult.Builder<FlightConnection>().data(it).localContext(filter).build() }

    @DgsData(parentType = DgsConstants.FLIGHTCONNECTION.TYPE_NAME, field = DgsConstants.FLIGHTCONNECTION.TotalCount)
    fun flightsTotalCount(dfe: DgsDataFetchingEnvironment): Int {
        val filter = dfe.getLocalContext<GraphqlFlightsFilter>()
        return flightRepository.count(filter.toDto())
    }

    @DgsEntityFetcher(name = DgsConstants.FLIGHT.TYPE_NAME)
    fun fetchFlight(values: Map<String, Any>, dfe: DgsDataFetchingEnvironment): CompletableFuture<Flight> {
        val id = values[DgsConstants.NODE.Id] as String
        val loader = dfe.getDataLoader<FlightId, Flight>(FlightsDataLoader::class.java)
        return loader.load(id.toInt()).thenApply {
            it ?: throw DgsEntityNotFoundException("Flight with id $id not found")
        }
    }
}

@DgsDataLoader(name = "flightsLoader")
class FlightsDataLoader(private val flightRepository: FlightRepository, private val dataLoaderExecutor: Executor) :
    MappedBatchLoader<FlightId, Flight> {
    override fun load(keys: Set<FlightId>): CompletionStage<Map<FlightId, Flight>> =
        CompletableFuture.supplyAsync(
            { flightRepository.getByIds(keys).mapValues { it.value.toGraphql() } },
            dataLoaderExecutor
        )
}

private fun DtoFlight.toGraphql(): Flight =
    Flight(id = { id.toString() }, startTime = { startTime }, landingTime = { landingTime }, planeSign = { planeSign })

private fun GraphqlFlightsFilter?.toDto(): DtoFlightsFilter? = this?.let { DtoFlightsFilter(plane = it.plane) }
