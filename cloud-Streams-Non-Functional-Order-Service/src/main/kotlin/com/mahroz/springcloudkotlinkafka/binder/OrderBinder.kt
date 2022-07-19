package com.mahroz.springcloudkotlinkafka.binder

import com.mahroz.springcloudkotlinkafka.dto.Order
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.Output
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel
import org.springframework.stereotype.Service



// non-functional approach to send and receive data from kafka , also called as pubSub approach
@Service
interface OrderBinder {

    /**
     * Producer & Consumer for inventoryChecking
     * -out and -in shows producer and consumer
     */
    @Output("inventoryChecking-out")      //produces order data that need to checked to inventory
    fun inventoryCheckingOut(): MessageChannel

    @Input("inventoryChecking-in")       // Consumes data and check if the stock is available
    fun inventoryCheckingIn(): SubscribableChannel


    /**
     * Producer & Consumer for shipping
     * -out and -in shows producer and consumer
     */
    @Output("shipping-out")   // produces data that need to be transfers for shipping
    fun shippingOut(): MessageChannel

    @Input("shipping-in")    // Consumes data and ship it to customer
    fun shippingIn(): SubscribableChannel


    /**
     * If any error occurs during the data consumption , payload is dumped to error topic
     *  order-dlq is consumer and scs-100.ordering_dlq is producer that was defined in yml using dlqName property
     */
    @Input("order-dlq")
    fun onOrderFailure():SubscribableChannel;

}