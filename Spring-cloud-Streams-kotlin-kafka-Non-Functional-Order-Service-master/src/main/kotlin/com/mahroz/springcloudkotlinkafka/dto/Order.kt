package com.mahroz.springcloudkotlinkafka.dto

import com.mahroz.springcloudkotlinkafka.utill.OrderStatus
import org.springframework.lang.Nullable
import java.util.UUID

class Order( var orderId:String, var itemName:String, var orderStatus:OrderStatus) {
}