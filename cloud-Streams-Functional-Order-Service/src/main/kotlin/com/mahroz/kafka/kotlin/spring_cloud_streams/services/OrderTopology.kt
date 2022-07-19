package com.mahroz.kafka.kotlin.spring_cloud_streams.services

import com.mahroz.kafka.kotlin.spring_cloud_streams.dto.Order
import com.mahroz.kafka.kotlin.spring_cloud_streams.utill.OrderStatus
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.utils.Bytes
import org.apache.kafka.streams.kstream.*
import org.apache.kafka.streams.state.KeyValueStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.util.function.Function
import java.util.logging.Level
import java.util.logging.Logger


@Service
class OrderTopology {

    var logger: Logger =Logger.getLogger("OrderTopology");

    var isOrderPlacedPredicate:Predicate<String,Order> =Predicate  {_,order -> order.orderStatus == OrderStatus.PENDING }
    var isInventoryCheckedPredicate:Predicate<String,Order> = Predicate { _, order -> order.orderStatus==OrderStatus.INVENTORY_CHECKING };
    var isShippedPredicate:Predicate<String,Order> = Predicate { _, order -> order.orderStatus==OrderStatus.SHIPPED };

    @Value("\${spring.application.name}")
    var kTableName:String="";


    /**
     * Below is KStream topology means how data float and consume through kafka
     * It takes orders , group them by key and then use aggregate function to create KTable
     * Commonly count, reduce and aggregate used for creating KTable
     * Below processor calls everytime when the state of order changes. Configuration are over application.yml
     */
    @Bean
    fun orderStateStoreProcessor(): Function<KStream<String,Order>,KStream<String,Order>> {
        return Function<KStream<String, Order>, KStream<String, Order>> { kStream ->

            logger.log(Level.INFO,"STREAM PROCESSOR CALLED $kStream");
            var orderStatusKTable: KTable<String, String> = kStream
                .peek{orderId,order -> logger.log(Level.INFO,"KTABLE STATUS $orderId  ${order.orderStatus}") }
                .groupByKey()
                .aggregate(
                    { String() }, // key
                    { _, v, _ ->  v.orderStatus.toString()}, // value to be stored
                    Materialized.`as`<String,String,KeyValueStore<Bytes,ByteArray>>(kTableName).withKeySerde(Serdes.String()).withValueSerde(Serdes.String())

                );


            /*
            Joining KTable with stream by using value joiner ( joins two values into a new value )
             */
            kStream.leftJoin(
                orderStatusKTable,
                ValueJoiner { v1, _ -> v1 }
            )

            kStream
        }
    }


}