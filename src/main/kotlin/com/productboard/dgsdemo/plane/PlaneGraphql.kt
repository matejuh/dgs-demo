package com.productboard.dgsdemo.plane

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.DgsDataLoader
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import com.productboard.dgsdemo.dgs.DgsConstants
import com.productboard.dgsdemo.dgs.types.AddPlaneInput
import com.productboard.dgsdemo.dgs.types.AddPlanePayload
import com.productboard.dgsdemo.dgs.types.AddPlaneSuccess
import com.productboard.dgsdemo.dgs.types.Flight as GraphqlFlight
import com.productboard.dgsdemo.dgs.types.Plane as GraphqlPlane
import com.productboard.dgsdemo.dgs.types.PlaneAlreadyExists
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.Executor
import org.dataloader.MappedBatchLoader

@DgsComponent
class PlaneGraphql(private val planeRepository: PlaneRepository) {
    @DgsMutation
    fun addPlane(@InputArgument input: AddPlaneInput): AddPlanePayload {
        val existingPlane = planeRepository.get(input.sign)
        if (existingPlane != null) {
            return PlaneAlreadyExists(existingPlane.toGraphql())
        }
        val createdPlane = planeRepository.create(input.toDto())
        return AddPlaneSuccess(createdPlane.toGraphql())
    }

    @DgsData(parentType = DgsConstants.FLIGHT.TYPE_NAME, field = DgsConstants.FLIGHT.Plane)
    fun flightPlane(dfe: DgsDataFetchingEnvironment): CompletableFuture<GraphqlPlane> {
        val flight = dfe.getSource<GraphqlFlight>()
        val loader = dfe.getDataLoader<PlaneSign, GraphqlPlane>(PlanesDataLoader::class.java)
        return loader.load(flight.planeSign)
    }
}

@DgsDataLoader(name = "planesLoader")
class PlanesDataLoader(
    private val planeRepository: PlaneRepository,
    private val dataLoaderExecutor: Executor,
) : MappedBatchLoader<PlaneSign, GraphqlPlane> {
    override fun load(keys: Set<PlaneSign>): CompletionStage<Map<PlaneSign, GraphqlPlane>> =
        CompletableFuture.supplyAsync(
            { planeRepository.getByIds(keys).mapValues { it.value.toGraphql() } },
            dataLoaderExecutor
        )
}

private fun AddPlaneInput.toDto(): CreatePlane = CreatePlane(sign = sign, type = type)

private fun Plane.toGraphql() = GraphqlPlane(sign = sign, type = type)
