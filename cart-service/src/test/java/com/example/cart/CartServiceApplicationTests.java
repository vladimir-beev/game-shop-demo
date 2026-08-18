package com.example.cart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "TOPIC_CHECKOUT=test.checkout",
        "KAFKA_BOOTSTRAP_SERVERS=test:9092",
        "PRODUCT_SERVICE_URL=localhost:1"
})
class CartServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
