package com.mahroz.kafka.kotlin.spring_cloud_streams.services

import com.mahroz.kafka.kotlin.spring_cloud_streams.dto.Order
import com.mahroz.kafka.kotlin.spring_cloud_streams.kafka_repo.OrderProducer
import com.mahroz.kafka.kotlin.spring_cloud_streams.utill.OrderStatus
import org.apache.kafka.streams.kstream.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.function.Consumer
import java.util.function.Function
import java.util.logging.Level
import java.util.logging.Logger


@Service
class OrderServiceImpl(val orderProducer: OrderProducer,val orderTopology:OrderTopology):OrderService {

    var logger: Logger =Logger.getLogger("OrderServiceImpl");


    @Value("\${spring.cloud.stream.bindings.inventoryCheck-in-0.destination}")
    var inventoryCheckTopic:String="";

    @Value("\${spring.cloud.stream.bindings.shipping-in-0.destination}")
    var shippingTopic:String="";

    @Value("\${spring.cloud.stream.bindings.shippedConsumer-in-0.destination}")
    var shippedTopic:String="";


    /**
     * Applying place order logic i.e save to db or change status
     * sending order to Kafka topic for further processing
     */
    override fun placeOrder(): Function<Order, Order> {
        return Function<Order, Order> { orderIn ->
            orderIn.orderStatus=OrderStatus.PENDING

            logger.log(Level.INFO,"Order saved ${orderIn.orderStatus}");
            orderProducer.sendOrder(orderIn)  // sending order to kafka topic
            orderIn   // return


        }
    }


    /**
     * Tables order stream , split stream by order status and send to related topics using branch.
     */
    @Bean
    fun orderProcess(): Function<KStream<String,Order>,KStream<String,Order>> {
       return Function<KStream<String,Order>,KStream<String,Order>> {kStream ->

            logger.log(Level.INFO,"Stream of orders received for inventory checking $kStream");
            kStream
                .peek{orderId,order -> logger.log(Level.INFO,"Routing Order $orderId  ${order.orderStatus}") }
                .split()
                .branch(orderTopology.isOrderPlacedPredicate,Branched.withConsumer{ks->ks.to(inventoryCheckTopic)})
                .branch(orderTopology.isInventoryCheckedPredicate, Branched.withConsumer{ks->ks.to(shippingTopic)})
                .branch(orderTopology.isShippedPredicate, Branched.withConsumer{ks->ks.to(shippedTopic)})
            kStream

        }
    }


    /**
     * Takes Order stream as passed by order process  and apply inventory business logic
     */
    @Bean
    fun inventoryCheck(): Function<KStream<String,Order>,KStream<String,Order>> {
        return Function<KStream<String, Order>, KStream<String, Order>> { kStream ->
            kStream
                .peek{_,v->logger.log(Level.INFO,"Applying Inventory Checking Process ${v.orderStatus}")}
                .mapValues { v-> v.orderStatus=OrderStatus.INVENTORY_CHECKING }

            kStream
        }
    }


    /**
     * Takes Order stream as passed by order process  and apply shipping business logic
     */
    @Bean
    fun shipping(): Function<KStream<String,Order>,KStream<String,Order>> {
        return Function<KStream<String, Order>, KStream<String, Order>> { kStream ->
            kStream
                .peek{_,v->logger.log(Level.INFO,"Applying Shipping process ${v.orderStatus}")}
                .mapValues { v-> v.orderStatus=OrderStatus.SHIPPED }

            kStream
        }
    }

    /**
     * Below is final step of order topology.
     * Consuming the final state of order stream
     */
    @Bean
    fun shippedConsumer():Consumer<KStream<String,Order>>{
        return Consumer<KStream<String,Order>> { kStream->
            kStream.
               foreach { _, order -> logger.log(Level.INFO,"Consumer called ${order.orderStatus}") }

            kStream
        }
    }




}