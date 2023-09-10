package com.ff.intg;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

class InvoiceWorkflowTriggerProcessor implements Processor {
    @Autowired
    WorkflowClient client;

    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("InvoicePdfWorkflowQueue")
                .setWorkflowId("invoice-json-pdf")
                .build();
        WorkflowClient client = WorkflowClient.newInstance(service);

        InvoicePdfWorkflow workflow =
                client.newWorkflowStub(
                        InvoicePdfWorkflow.class, options);
        workflow.execute(body);
    }

}

@Component
public class InvoiceWorkflowTrigger extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("file-watch://invoices?events=CREATE&antInclude=**/*.json")
                .log("File event: ${header.CamelFileEventType} occurred on file ${header.CamelFileName} at ${header.CamelFileLastModified}")
                .process(new InvoiceWorkflowTriggerProcessor())
        ;
    }
}
