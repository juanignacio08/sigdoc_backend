package com.sigdoc.sigdoc_backend.expose.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityUtil {
    public static Long getCurrentUserId() {
        Long TEMPORARY_USER_ID = 1L;
        return TEMPORARY_USER_ID;
    }
}
