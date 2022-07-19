package com.mahroz.kafka.kotlin.spring_cloud_streams.dto

import com.mahroz.kafka.kotlin.spring_cloud_streams.utill.OrderStatus

class Order(var orderId:String, var itemName:String, var orderStatus:OrderStatus) {
}