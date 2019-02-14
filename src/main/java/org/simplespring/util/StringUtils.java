package org.simplespring.util;

import com.sun.istack.internal.Nullable;

public class StringUtils {
    /**
     * Check that the given {@code String} is neither {@code null} nor of length 0.
     */
    public static boolean hasLength(@Nullable String str) {
        return (str != null && !str.isEmpty());
    }
}
