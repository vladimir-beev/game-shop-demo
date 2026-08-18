package com.example.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.kafka.listener.auto-startup=false",
        "spring.kafka.bootstrap-servers=localhost:1",
        "topic.order-cancelled=test.order.cancelled",
        "topic.order-created=test.order.created",
        "topic.stock-rejected=test.stock.rejected"
})
class InventoryServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
