package com.productboard.dgsdemo.flight

import com.netflix.graphql.dgs.client.jsonTypeRef
import com.productboard.dgsdemo.DgsDemoApplicationTests
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FlightGraphqlTest : DgsDemoApplicationTests() {

    @Test
    fun `Should list`() {
        val response =
            graphQLClient.executeQuery(
                """
            query ListFlights {
                listFlights(first: 10) {
                    edges {
                        node {
                            id
                        }
                    }
                }
            }
            """
            )

        val ids = response.extractValueAsObject("data.listFlights.edges[*].node.id", jsonTypeRef<List<String>>())
        assertThat(ids).hasSize(10)
    }

    @Test
    fun `Should list with filter`() {
        val response =
            graphQLClient.executeQuery(
                """
            query ListFlights {
                listFlights(first: 10, filter: { plane: "OK-6400" }) {
                    edges {
                        node {
                            plane {
                                type
                            }
                        }
                    }
                }
            }
            """
            )

        val types =
            response.extractValueAsObject("data.listFlights.edges[*].node.plane.type", jsonTypeRef<List<String>>())
        assertThat(types.distinct()).containsExactly("Std. Cirrus")
    }

    @Test
    fun `Should get flights edges and total count`() {
        val countQuery =
            graphQLClient.executeQuery(
                """
            query CountFlights {
                listFlights(first: 2, filter: { plane: "OK-6400" }) {
                    edges {
                        node {
                            id
                        }
                    }
                    totalCount
                }
            }
            """
            )
        assertThat(countQuery.extractValueAsObject("data.listFlights.totalCount", Int::class.java)).isEqualTo(4)
        assertThat(countQuery.extractValueAsObject("data.listFlights.edges.length()", Int::class.java)).isEqualTo(2)
    }
}
