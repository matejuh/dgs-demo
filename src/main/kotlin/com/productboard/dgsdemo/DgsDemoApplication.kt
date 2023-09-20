package com.productboard.dgsdemo

import java.util.concurrent.Executor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@SpringBootApplication class DgsDemoApplication

fun main(args: Array<String>) {
    runApplication<DgsDemoApplication>(*args)
}

@Bean
fun dataLoaderExecutor(): Executor {
    val executor = ThreadPoolTaskExecutor()
    executor.corePoolSize = 10
    executor.maxPoolSize = 100
    executor.queueCapacity = 50
    executor.setThreadNamePrefix("dgs-data-loader-worker-")
    executor.initialize()
    return executor
}
