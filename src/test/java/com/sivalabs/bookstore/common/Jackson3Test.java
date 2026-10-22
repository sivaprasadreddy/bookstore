package com.sivalabs.bookstore.common;

import static tools.jackson.core.StreamWriteFeature.IGNORE_UNKNOWN;
import static tools.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.sivalabs.bookstore.common.model.Address;
import com.sivalabs.bookstore.common.model.Customer;
import com.sivalabs.bookstore.orders.events.OrderCancelledEvent;
import java.util.Set;
import tools.jackson.databind.json.JsonMapper;

public class Jackson3Test {
    static void main() {
        JsonMapper jsonMapper = JsonMapper.builder()
                // .addModules(SecurityJacksonModules.getModules(this.loader))
                // .addMixIn(SecurityUser.class, SecurityUserMixin.class)
                .enable(IGNORE_UNKNOWN)
                .disable(FAIL_ON_UNKNOWN_PROPERTIES)
                /*.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build(),
                DefaultTyping.NON_FINAL_AND_ENUMS,
                JsonTypeInfo.As.PROPERTY)*/
                .build();
        TestBean testBean = new TestBean(1L, "test");
        System.out.println(jsonMapper.writeValueAsString(testBean));

        OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(
                "ord-123", "testing", Set.of(), new Customer("sds", "sds", "fdf"), new Address("", "", "", "", "", ""));
        String json = jsonMapper.writeValueAsString(orderCancelledEvent);
        System.out.println(json);

        OrderCancelledEvent orderCancelledEvent1 = jsonMapper.readValue(json, OrderCancelledEvent.class);
        System.out.println(orderCancelledEvent1);
    }
}

record TestBean(Long id, String name) {}
