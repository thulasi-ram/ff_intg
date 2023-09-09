package com.ff.intg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@JsonIgnoreProperties(ignoreUnknown = true)
class InvoiceEvent {
    @JsonProperty("account_id")
    public String accountID;

    public String getAccountID() {
        return accountID;
    }
}

@Component
public class InvoiceRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("spring-rabbitmq:invoices_exchange?queues=invoices_queue&routingKey=invoices.*").unmarshal().json(JsonLibrary.Jackson)
                .setHeader("accountID", simple("${body.get('account_id')}"))
                .setHeader("invoiceID", simple("${body.get('payload').get('invoice').get('entity').get('id')}"))
                .setHeader("CamelFileName", simple("${headers.invoiceID}.json"))
                .marshal().json()
                .toD("file:invoices/${headers.accountID}")
                .convertBodyTo(String.class)
                .setHeader("auditEntityID", simple("${headers.invoiceID}"))
                .setHeader("auditEntityType", constant("create_invoice"))
                .setHeader("auditEntityPayload", simple("${body.toString()}"))
                .to("sql:classpath:sql/invoice_audit.sql")
        ;
    }

}
