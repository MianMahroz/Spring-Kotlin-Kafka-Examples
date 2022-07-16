package com.mahroz.springcloudkotlinkafka.repo

import com.mahroz.springcloudkotlinkafka.dto.Order
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class OrderRepo {

    /**
     * We have used hashmap to store data, but you can use actual database also.
     */
    var hashmap=HashMap<String,Order>();


    fun saveOrder(order:Order):Order{
        hashmap[order.orderId] = order;
        return order;
    }

}