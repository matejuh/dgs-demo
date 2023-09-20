package com.productboard.dgsdemo

import com.netflix.graphql.dgs.client.GraphQLClient
import com.netflix.graphql.dgs.client.RequestExecutor
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.test.context.TestPropertySource
import org.springframework.util.MultiValueMapAdapter

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["logbook.strategy=default", "logbook.format.style=http"])
class DgsDemoApplicationTests {

    @LocalServerPort private var randomServerPort: Int = 0

    protected lateinit var graphQLClient: GraphQLClient

    protected val requestExecutor: RequestExecutor = RequestExecutor { url, headers, body ->
        // Prepare the request, e.g. set up headers.
        val requestBuilder =
            HttpRequest.newBuilder(URI.create(url)).method("POST", HttpRequest.BodyPublishers.ofString(body))
        headers.forEach { header ->
            header.value.forEach { headerValue -> requestBuilder.header(header.key, headerValue) }
        }
        // Use your HTTP client to send the request to the server.
        val client = HttpClient.newHttpClient()
        // Transform the response into a HttpResponse
        client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString()).let {
            com.netflix.graphql.dgs.client.HttpResponse(it.statusCode(), it.body())
        }
    }

    @BeforeEach
    protected fun setup() {
        if (!::graphQLClient.isInitialized) {
            graphQLClient =
                GraphQLClient.createCustom("http://localhost:$randomServerPort/graphql") { url, headers, body ->
                    requestExecutor.execute(url, HttpHeaders().apply { addAll(MultiValueMapAdapter(headers)) }, body)
                }
        }
    }
}
