package com.mahroz.springcloudkotlinkafka

import com.mahroz.springcloudkotlinkafka.binder.OrderBinder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding

@SpringBootApplication
@EnableBinding(OrderBinder::class)   // declaring kafka binding class
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}
