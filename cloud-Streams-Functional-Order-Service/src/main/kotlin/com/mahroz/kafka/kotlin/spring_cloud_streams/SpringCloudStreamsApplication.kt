package com.mahroz.kafka.kotlin.spring_cloud_streams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafkaStreams

@SpringBootApplication
//@EnableKafkaStreams
class SpringCloudStreamsApplication

fun main(args: Array<String>) {
	runApplication<SpringCloudStreamsApplication>(*args)
}
