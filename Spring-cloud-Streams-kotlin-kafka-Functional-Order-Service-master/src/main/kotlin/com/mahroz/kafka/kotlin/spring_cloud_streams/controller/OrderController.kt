package com.mahroz.kafka.kotlin.spring_cloud_streams.controller

import com.mahroz.kafka.kotlin.spring_cloud_streams.dto.Order
import com.mahroz.kafka.kotlin.spring_cloud_streams.services.OrderService
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderController(val orderService: OrderService,val interactiveQueryService: InteractiveQueryService) {

    @Value("\${spring.application.name}")
    var kTableName:String="";

    /**
     * Takes order place request and processing it using order service
     */
    @PostMapping("saveOrder")
    fun saveOrder(@RequestBody order:Order): ResponseEntity<String> {
        orderService.placeOrder().apply(order);

        return ResponseEntity.ok("Order saved")
    }

    /**
     * We can use #interactiveQueryService to query data from the kTable that we created in #orderStateStoreProcessor
     * Takes order Id and returns the current status of the order
     */
    @GetMapping("status/{orderId}")
    fun getOrderStatus(@PathVariable(name = "orderId") orderId:String):ResponseEntity<String>{

        var store:ReadOnlyKeyValueStore<String, String> = interactiveQueryService.getQueryableStore(kTableName, QueryableStoreTypes.keyValueStore());
        return ResponseEntity.ok(store.get(orderId))
    }

}