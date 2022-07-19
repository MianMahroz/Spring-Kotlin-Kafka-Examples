package com.mahroz.kafka.kotlin.spring_cloud_streams.utill

enum class OrderStatus(name:String) {
    PENDING("PENDING"),
    INVENTORY_CHECKING("INVENTORY_CHECKING"),
    OUT_OF_STOCK("OUT_OF_STOCK"),
    SHIPPED("SHIPPED"),
    CANCELED("CANCELED")
}