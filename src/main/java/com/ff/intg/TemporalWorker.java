package com.ff.intg;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.stereotype.Component;

@Component
public class TemporalWorker {
    public void setup() {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);
        Worker worker = factory.newWorker("InvoicePdfWorkflowQueue");
        worker.registerWorkflowImplementationTypes(InvoicePdfWorkflowImpl.class);
        worker.registerActivitiesImplementations(new InvoicePdfActivityImpl());
        factory.start();
    }
}
