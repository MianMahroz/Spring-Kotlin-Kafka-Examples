package com.mahroz.springcloudkotlinkafka.controller

import com.mahroz.springcloudkotlinkafka.dto.Order
import com.mahroz.springcloudkotlinkafka.services.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@org.springframework.web.bind.annotation.RestController
class RestController(val orderService: OrderService) {


    @PostMapping("/saveOrder")
    fun saveOrder(@RequestBody order:Order): ResponseEntity<String> {
        orderService.placeOrder(order);
        return ResponseEntity.ok("order saved");
    }
}