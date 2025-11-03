package com.k1ts.check.taskcheckservice;

import com.k1ts.check.request.check.CheckResponse;

public interface TaskCheckService {
    CheckResponse check(String code);
}
