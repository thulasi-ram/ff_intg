package com.ff.intg;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConsumer extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("spring-rabbitmq:invoices_exchange?queues=invoices_queue&routingKey=invoices.*")
                .routeId("InvoiceConsumer")
                .log(LoggingLevel.INFO, "consuming invoice")
                .unmarshal().json(JsonLibrary.Jackson)
                .setHeader("accountID", simple("${body.get('account_id')}"))
                .setHeader("invoiceID", simple("${body.get('invoice').get('id')}"))
                .setHeader("CamelFileName", simple("${headers.invoiceID}.json"))
                .marshal().json()
//                .toD("file:invoices/${headers.accountID}")
                .toD("file:invoices")
                .convertBodyTo(String.class)
                .setHeader("auditEntityID", simple("${headers.invoiceID}"))
                .setHeader("auditEntityType", constant("create_invoice"))
                .setHeader("auditEntityPayload", simple("${body.toString()}"))
                .to("sql:classpath:sql/invoice_audit.sql")
        ;
    }

}
