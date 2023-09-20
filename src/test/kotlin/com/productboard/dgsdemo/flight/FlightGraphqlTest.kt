package com.productboard.dgsdemo.flight

import com.netflix.graphql.dgs.client.jsonTypeRef
import com.productboard.dgsdemo.DgsDemoApplicationTests
import com.productboard.dgsdemo.dgs.DgsClient
import com.productboard.dgsdemo.dgs.DgsConstants
import com.productboard.dgsdemo.dgs.types.FlightsFilter
import com.productboard.dgsdemo.plane.PlaneSign
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FlightGraphqlTest : DgsDemoApplicationTests() {

    @Test
    fun `Should list`() {
        val listQuery = DgsClient.buildQuery { listFlights(first = 10) { edges { node { id } } } }

        val ids =
            graphQLClient
                .executeQuery(listQuery)
                .extractValueAsObject("data.listFlights.edges[*].node.id", jsonTypeRef<List<String>>())
        assertThat(ids).hasSize(10)
    }

    @Test
    fun `Should list with filter`() {
        val listWithFilterQuery =
            DgsClient.buildQuery {
                listFlights(first = 10, filter = FlightsFilter(plane = "OK-6400")) { edges { node { plane { type } } } }
            }
        val response = graphQLClient.executeQuery(listWithFilterQuery)

        val types =
            response.extractValueAsObject("data.listFlights.edges[*].node.plane.type", jsonTypeRef<List<String>>())
        assertThat(types.distinct()).containsExactly("Std. Cirrus")
    }

    @Test
    fun `Should get flights edges and total count`() {
        val countQuery =
            graphQLClient.executeQuery(
                DgsClient.buildQuery {
                    listFlights(first = 2, filter = FlightsFilter(plane = "OK-6400")) {
                        edges { node { id } }
                        totalCount
                    }
                }
            )

        assertThat(countQuery.extractValueAsObject("data.listFlights.totalCount", Int::class.java)).isEqualTo(4)
        assertThat(countQuery.extractValueAsObject("data.listFlights.edges.length()", Int::class.java)).isEqualTo(2)
    }

    @Test
    fun `Should get single flight through typed node interface`() {
        val query =
            """
            query (${'$'}representations: [_Any!]!) {
              _entities(representations: ${'$'}representations) {
                ... on Flight {
                  plane {
                    sign
                  }
                }
              }  
            }"""
        val variables =
            mapOf("representations" to listOf(mapOf("__typename" to DgsConstants.FLIGHT.TYPE_NAME, "id" to "1")))

        val response = graphQLClient.executeQuery(query, variables)
        assertThat(response.extractValueAsObject("data._entities[0].plane.sign", PlaneSign::class.java))
            .isEqualTo("OK-6400")
    }

    @Test
    fun `Should return error when trying to get not existing flight`() {
        val query =
            """
            query (${'$'}representations: [_Any!]!) {
              _entities(representations: ${'$'}representations) {
                ... on Flight {
                  plane {
                    sign
                  }
                }
              }  
            }"""
        val variables =
            mapOf("representations" to listOf(mapOf("__typename" to DgsConstants.FLIGHT.TYPE_NAME, "id" to "758545")))

        val response = graphQLClient.executeQuery(query, variables)
        assertThat(response.errors[0].message).isEqualTo("Flight with id 758545 not found")
        assertThat(response.extractValueAsObject("data._entities[0]", String::class.java)).isNull()
    }
}
