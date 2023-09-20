package com.productboard.dgsdemo.plane

import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import com.productboard.dgsdemo.DgsDemoApplicationTests
import com.productboard.dgsdemo.dgs.client.AddPlaneGraphQLQuery
import com.productboard.dgsdemo.dgs.client.AddPlaneProjectionRoot
import com.productboard.dgsdemo.dgs.types.AddPlaneInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PlaneGraphqlTest : DgsDemoApplicationTests() {
    @Autowired private lateinit var dgsQueryExecutor: DgsQueryExecutor

    @Test
    fun `Should add plane`() {
        val mutation =
            GraphQLQueryRequest(
                AddPlaneGraphQLQuery.newRequest().input(AddPlaneInput(sign = "OK-4832", type = "L-13")).build(),
                AddPlaneProjectionRoot<Nothing, Nothing>().onAddPlaneSuccess().plane().sign()
            )
        assertThat(
                dgsQueryExecutor.executeAndExtractJsonPath<PlaneSign>(mutation.serialize(), "data.addPlane.plane.sign")
            )
            .isEqualTo("OK-4832")
    }
}
