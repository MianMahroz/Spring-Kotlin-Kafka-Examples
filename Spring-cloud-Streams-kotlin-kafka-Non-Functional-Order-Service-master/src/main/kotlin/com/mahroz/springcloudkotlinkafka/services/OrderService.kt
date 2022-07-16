package com.mahroz.springcloudkotlinkafka.services

import com.mahroz.springcloudkotlinkafka.dto.Order
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service


@Service
interface OrderService {

    fun placeOrder(order:Order):Order;
    fun inventoryChecking(@Payload order:Order):Order;
    fun shipIt(@Payload order:Order):Unit;

    }