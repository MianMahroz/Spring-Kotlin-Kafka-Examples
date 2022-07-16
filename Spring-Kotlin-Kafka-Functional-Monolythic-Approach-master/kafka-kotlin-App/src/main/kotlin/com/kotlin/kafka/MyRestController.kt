package com.kotlin.kafka

import org.apache.kafka.streams.state.QueryableStoreTypes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MyRestController {

    @Autowired
    private lateinit var iqService: InteractiveQueryService

    @GetMapping("count/{word}")
    fun getCount(@PathVariable word:String): Long {
        val store=iqService.getQueryableStore("word-count-state-store",QueryableStoreTypes.keyValueStore<String,Long>())
//        for (keyValue in store.all()) {
//            println(keyValue.value);
//            println(keyValue.key);
//        }
        return store[word];
    }

}