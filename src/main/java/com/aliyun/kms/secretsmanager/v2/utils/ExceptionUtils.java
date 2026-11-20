package com.aliyun.kms.secretsmanager.v2.utils;


import com.aliyun.tea.TeaException;
import com.aliyuncs.kms.secretsmanager.client.v2.exception.CacheSecretException;

public class ExceptionUtils {

    private static final String FORBIDDEN_RESOURCE_NOT_FOUND_ERROR_CODE = "Forbidden.ResourceNotFound";

    private ExceptionUtils() {
        // do nothing
    }

    public static boolean isResourceNotFound(CacheSecretException e) {
        if (e.getCause() instanceof TeaException && ((TeaException) e.getCause()).getCode().equals(FORBIDDEN_RESOURCE_NOT_FOUND_ERROR_CODE)) {
            return true;
        }
        return false;
    }
}
