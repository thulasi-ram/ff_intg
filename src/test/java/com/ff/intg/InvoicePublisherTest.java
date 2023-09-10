package com.ff.intg;

import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.NotifyBuilderMatcher;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.apache.camel.builder.NotifyBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.apache.camel.builder.Builder.body;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@CamelSpringBootTest
@MockEndpoints("mock:spring-rabbitmq:invoices_exchange")
class InvoicePublisherTest {

    @Autowired
    private ProducerTemplate template;

    @Autowired
    private CamelContext camelContext;

    @Test
    public void testConfigureTimer() throws Exception {
        NotifyBuilder notify = new NotifyBuilder(camelContext).whenDone(1).create();
        assertTrue(notify.matches(5, TimeUnit.SECONDS));
    }

//    @Test
//    public void testConfigureOutput() throws Exception {
//        Predicate bodyContains = body().contains("invoice");
//        NotifyBuilder notify = new NotifyBuilder(camelContext)
//                .waitTime(5).whenDone(1).whenAnyReceivedMatches(bodyContains).create();
//        assertTrue(notify.matches(5, TimeUnit.SECONDS));
//    }
}