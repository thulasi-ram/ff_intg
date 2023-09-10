package com.ff.intg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Random;


class Invoice {
    @JsonProperty("id")
    public String id;


    public Invoice() {
        this.id = "inv_" + (new Random()).nextInt(1000);
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class InvoiceEvent {
    @JsonProperty("account_id")
    public String accountID;

    public Invoice invoice;

    public InvoiceEvent() {
        this.accountID = "acc_" + (new Random()).nextInt(1000);
        this.invoice = new Invoice();
    }
}

class InvoiceCamelProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InvoiceEvent event = new InvoiceEvent();
        String json = mapper.writeValueAsString(event);
        exchange.getIn().setBody(json);
    }

}

@Component
public class InvoicePublisher extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer:publish_invoices_every_second?period=30000")
                .routeId("InvoicePublisher")
                .process(new InvoiceCamelProcessor())
                .log(LoggingLevel.INFO, "Publishing message:${body}").
                to("spring-rabbitmq:invoices_exchange?routingKey=invoices.create");
    }
}
