package com.ff.intg;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.direct.DirectComponent;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.annotations.Component;
import org.apache.camel.support.HeaderFilterStrategyComponent;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.camel.builder.Builder.body;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@CamelSpringBootTest
@MockEndpoints("mock:spring-rabbitmq:invoices_exchange")
class InvoiceConsumerTest {

    @Autowired
    private ProducerTemplate template;

    @EndpointInject("mock:spring-rabbitmq:invoices_exchange")
    private MockEndpoint mock;
    @Autowired
    private CamelContext camelContext;

    @Test
    public void testBody() throws Exception {
        MockEndpoint resultEndpoint = camelContext.getEndpoint("mock:spring-rabbitmq:invoices_exchange", MockEndpoint.class);

        ObjectMapper mapper = new ObjectMapper();
        InvoiceEvent event = new InvoiceEvent();
        String json = mapper.writeValueAsString(event);

        resultEndpoint.expectedBodiesReceived(json);
        template.sendBody("mock:spring-rabbitmq:invoices_exchange", json);
        resultEndpoint.assertIsSatisfied();
    }

//    @Test
//    public void testLog() throws Exception {
//        // https://stackoverflow.com/questions/25463356/how-to-mock-amqp-consumers-in-camel-testing
//        ObjectMapper mapper = new ObjectMapper();
//        InvoiceEvent event = new InvoiceEvent();
//        String json = mapper.writeValueAsString(event);
//
//        camelContext.removeComponent("spring-rabbitmq");
//        camelContext.addComponent("spring-rabbitmq", this.camelContext.getComponent("default"));
//
//        MockEndpoint logEndpoint = camelContext.getEndpoint("mock:log", MockEndpoint.class);
//        template.sendBody("spring-rabbitmq", json);
//        logEndpoint.expectedMessagesMatches(body().contains("consuming invoice"));
//        logEndpoint.assertIsSatisfied();
//    }

}