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
}
