package com.productboard.dgsdemo.plane

import com.productboard.dgsdemo.DgsDemoApplicationTests
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PlaneRepositoryTest : DgsDemoApplicationTests() {
    @Autowired private lateinit var planeRepository: PlaneRepository

    @Test
    fun `Should get`() {
        assertThat(planeRepository.get("OK-7761")?.type).isEqualTo("Duo Discus")
    }

    @Test
    fun `Should create`() {
        val plane = planeRepository.create(CreatePlane(sign = "OK-7427", type = "L-33"))
        assertThat(plane.type).isEqualTo("L-33")
        assertThat(plane.sign).isEqualTo("OK-7427")
        assertThat(planeRepository.get("OK-7427")).isNotNull
    }
}
