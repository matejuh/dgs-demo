package com.productboard.dgsdemo

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.TestPropertySource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = ["logbook.strategy=default", "logbook.format.style=http"])
class DgsDemoApplicationTests {

    @LocalServerPort private var randomServerPort: Int = 0
}
