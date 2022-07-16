package com.mahroz.kafka.kotlin.spring_cloud_streams.services

import com.mahroz.kafka.kotlin.spring_cloud_streams.dto.Order
import org.apache.kafka.streams.kstream.KStream
import org.springframework.stereotype.Service
import java.util.function.Function

@Service
interface OrderService {

    fun placeOrder(): Function<Order, Order> ;
}