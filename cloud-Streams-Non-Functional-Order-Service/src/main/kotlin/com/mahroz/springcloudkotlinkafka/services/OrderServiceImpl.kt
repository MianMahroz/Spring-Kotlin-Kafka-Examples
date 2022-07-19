package com.mahroz.springcloudkotlinkafka.services

import com.mahroz.springcloudkotlinkafka.binder.OrderBinder
import com.mahroz.springcloudkotlinkafka.dto.Order
import com.mahroz.springcloudkotlinkafka.repo.OrderRepo
import com.mahroz.springcloudkotlinkafka.utill.OrderStatus
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import java.util.logging.Level
import java.util.logging.Logger

@Service
class OrderServiceImpl(val orderRepo:OrderRepo, val orderBinder: OrderBinder) :OrderService{

    var logger: Logger =Logger.getLogger("");


    /**
     * Saves order data to db and then forwards to #inventoryChecking-out for further processing
     */
    override fun placeOrder(order: Order):Order {
        order.orderStatus=OrderStatus.PENDING
        orderRepo.saveOrder(order)
        logger.log(Level.INFO,"Order saved",order);

        // sending order to kafka topic  # inventoryChecking-out
        orderBinder.inventoryCheckingOut()
            .send(MessageBuilder
                .withPayload(order)
                .setHeader(MessageHeaders.CONTENT_TYPE,MimeTypeUtils.APPLICATION_JSON_VALUE)
                .build())
        return order;
    }




    /**
     * kafka stream, this can be an external microservice
     * This stream process data from #inventoryChecking-in  topic
     * @SentTo used to forward data to some other topic after processing
     */
    @StreamListener("inventoryChecking-in")
    @SendTo("shipping-out")
   override fun inventoryChecking(@Payload order:Order):Order{
        logger.log(Level.INFO,"Order RECEIVED",order);

        order.orderStatus=OrderStatus.CHECKING_STOCK;
        orderRepo.saveOrder(order)  // updating order

        /**
         * you can also send data to some other topic using below
         * It is equal to @SendTo
         */
//        orderBinder.shippingOut()
//            .send(MessageBuilder
//                .withPayload(order)
//                .setHeader(MessageHeaders.CONTENT_TYPE,MimeTypeUtils.APPLICATION_JSON_VALUE)
//                .build())


        return order;
    }




    /**
     *   This stream process data from #shipped-in
     *   It takes order and execute process to make it delivered to customer
     */

   @StreamListener("shipping-in")
   override fun shipIt(@Payload order:Order):Unit {

        logger.log(Level.INFO,"Order ready to be shipped ${order.orderStatus}");

        // updating order
        order.orderStatus=OrderStatus.SHIPPED;
        orderRepo.saveOrder(order);

        /**
         * Use below exception to test the error topic
         * On any error data dumped to scs-100.ordering_dlq and then consumed by order-dlq
         */
//        throw RuntimeException("Thread Interruption")


    }


    /**
     * Consuming Order`s that was not completed because of some issues
     */
    @StreamListener("order-dlq")
    fun dlqListener(@Payload order:Order):Unit{
        logger.log(Level.INFO,"ERROR ORDER DQL ${order.orderStatus}");

    }

}