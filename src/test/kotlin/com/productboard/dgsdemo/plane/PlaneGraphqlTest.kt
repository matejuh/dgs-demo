package com.productboard.dgsdemo.plane

import com.netflix.graphql.dgs.DgsQueryExecutor
import com.productboard.dgsdemo.DgsDemoApplicationTests
import com.productboard.dgsdemo.dgs.DgsClient
import com.productboard.dgsdemo.dgs.types.AddPlaneInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PlaneGraphqlTest : DgsDemoApplicationTests() {
    @Autowired private lateinit var dgsQueryExecutor: DgsQueryExecutor

    @Test
    fun `Should add plane`() {
        val mutation =
            DgsClient.buildMutation {
                addPlane(input = AddPlaneInput(sign = "OK-4832", type = "L-13")) {
                    onAddPlaneSuccess { plane { sign } }
                }
            }
        assertThat(dgsQueryExecutor.executeAndExtractJsonPath<PlaneSign>(mutation, "data.addPlane.plane.sign"))
            .isEqualTo("OK-4832")
    }

    @Test
    fun `Should return already existing on add`() {
        val mutation =
            DgsClient.buildMutation {
                addPlane(input = AddPlaneInput(sign = "OK-7761", type = "Discus 2b")) {
                    onPlaneAlreadyExists { plane { type } }
                }
            }

        assertThat(graphQLClient.executeQuery(mutation).extractValue<String>("data.addPlane.plane.type"))
            .isEqualTo("Duo Discus")
    }
}
