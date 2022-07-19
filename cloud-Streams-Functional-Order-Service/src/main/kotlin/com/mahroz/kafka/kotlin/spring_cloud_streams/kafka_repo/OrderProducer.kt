package com.mahroz.kafka.kotlin.spring_cloud_streams.kafka_repo

import com.mahroz.kafka.kotlin.spring_cloud_streams.dto.Order
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.kstream.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.stereotype.Repository


@Repository
class OrderProducer(val orderProducer: KafkaTemplate<String,Order>) {
    @Value("\${spring.cloud.stream.bindings.orderStateStoreProcessor-in-0.destination}")
    private val orderTopic: String="";

    fun sendOrder(order:Order){
        orderProducer.send(orderTopic,order.orderId,order)
    }





}