package com.ff.intg;


import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface InvoicePdfWorkflow {

    @WorkflowMethod
    void execute(String body) throws Exception;
}