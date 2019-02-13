package org.simplespring.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class AutowireUtils {
    public static void sortConstructors(Constructor<?>[] constructors) {
        Arrays.sort(constructors, (c1, c2) -> {
            boolean p1 = Modifier.isPublic(c1.getModifiers());
            boolean p2 = Modifier.isPublic(c2.getModifiers());
            if (p1 != p2) {
                return (p1 ? -1 : 1);
            }
            int c1pl = c1.getParameterCount();
            int c2pl = c2.getParameterCount();
            return (c1pl < c2pl ? 1 : (c1pl > c2pl ? -1 : 0));
        });
    }
}
