# Spring-cloud-kotlin-kafka-Non-Functional-Approach

# Pre-requisites

- Fire up docker-compose.yml using below comamand to make kafka instance up
- docker-compose -f docker-compose.yml up -d  


 No need to define kafka instance path i.e localhost:9092 spring cloud knows the default path 


**Example Concept**

-out & -in after topic names represents Producer and Consumer respctively

- User place order and send to inventoryChecking-out producer topic for further processing
- inventoryChecking-in consumer topic receives order and if the stock availble it forward to shipping-out producer topic
- Shipping-in consumer topic receives order at the end for shippment.
- During data consuption if any error occurs that order will be transfer to error topic named as scs-100.ordering_dlq


**applicaiton.yml only conntains consumer properties**


# Example follows Non-Function approach 


**Example Kafka Binder**

    /**
     * Producer & Consumer for inventoryChecking
     * -out and -in shows producer and consumer
     */
    @Output("inventoryChecking-out")      //produces order data that need to checked to inventory
    fun inventoryCheckingOut(): MessageChannel

    @Input("inventoryChecking-in")       // Consumes data and check if the stock is available
    fun inventoryCheckingIn(): SubscribableChannel


    /**
     * Producer & Consumer for shipping
     * -out and -in shows producer and consumer
     */
    @Output("shipping-out")   // produces data that need to be transfers for shipping
    fun shippingOut(): MessageChannel

    @Input("shipping-in")    // Consumes data and ship it to customer
    fun shippingIn(): SubscribableChannel


    /**
     * If any error occurs during the data consumption , payload is dumped to error topic
     *  order-dlq is consumer and scs-100.ordering_dlq is producer that was defined in yml using dlqName property
     */
    @Input("order-dlq")
    fun onOrderFailure():SubscribableChannel;


