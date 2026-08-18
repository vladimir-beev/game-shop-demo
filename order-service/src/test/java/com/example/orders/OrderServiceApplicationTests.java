package com.example.orders;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.kafka.listener.auto-startup=false",
        "spring.kafka.bootstrap-servers=localhost:1",
        "topic.checkout=test.checkout",
        "topic.order-created=test.order.created",
        "topic.stock-rejected=test.stock.rejected"
})
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
