package com.ff.intg;


import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;

import java.time.Duration;

@WorkflowImpl
public class InvoicePdfWorkflowImpl implements InvoicePdfWorkflow {

    private InvoicePdfActivity activity =
            Workflow.newActivityStub(
                    InvoicePdfActivity.class,
                    ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());


    @Override
    public void execute(String body) throws Exception {
        activity.jsonToPDF(body);
    }
}