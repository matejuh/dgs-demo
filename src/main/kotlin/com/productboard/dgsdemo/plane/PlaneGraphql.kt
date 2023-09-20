package com.productboard.dgsdemo.plane

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import com.productboard.dgsdemo.dgs.types.AddPlaneInput
import com.productboard.dgsdemo.dgs.types.AddPlanePayload
import com.productboard.dgsdemo.dgs.types.AddPlaneSuccess
import com.productboard.dgsdemo.dgs.types.Plane as GraphqlPlane
import com.productboard.dgsdemo.dgs.types.PlaneAlreadyExists

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
}

private fun AddPlaneInput.toDto(): CreatePlane = CreatePlane(sign = sign, type = type)

private fun Plane.toGraphql() = GraphqlPlane(sign = sign, type = type)
