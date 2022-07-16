package com.mahroz.springcloudkotlinkafka.utill

enum class OrderStatus(name: String) {
    PENDING("PENDING"),
    CHECKING_STOCK("CHECKING_STOCK"),
    OUT_OF_STOCK("OUT_OF_STOCK"),
    SHIPPED("SHIPPED"),
    CANCELLED("CANCELLED")

}