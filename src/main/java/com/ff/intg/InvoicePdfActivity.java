package com.ff.intg;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface InvoicePdfActivity {
    @ActivityMethod
    void jsonToPDF(String body) throws Exception;
}
