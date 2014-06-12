package net.argius.java8.seq;

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

final class TestUtils {

    private TestUtils() {
        //
    }

    private static void error(String msg) {
        throw new RuntimeException(msg);
    }

    static boolean hasMethod(Class<?> targetClass, String methodName) {
        // System.out.println("----------");
        if (isBlank(methodName))
            error("method name is empty");
        Predicate<? super Method> f = x -> x.getName().equals(methodName);
        for (Class<?> c : getAllRelatedClassesOf(targetClass)) {
            // System.out.printf(">>>? %s %s %n", c, methodName);
            // System.out.println(">>> " +
            // Stream.of(c.getDeclaredMethods()).filter(f).collect(Collectors.toList()));
            Optional<Method> r = Stream.of(c.getDeclaredMethods()).filter(f).findFirst();
            if (r.isPresent())
                return true;
        }
        return false;
    }

    private static Set<Class<?>> getAllRelatedClassesOf(Class<?> c) {
        Set<Class<?>> a = new LinkedHashSet<>();
        a.add(c);
        for (Class<?> class1 : c.getInterfaces())
            a.addAll(getAllRelatedClassesOf(class1));
        Class<?> superClass = c.getSuperclass();
        if (superClass != null)
            a.add(superClass);
        // System.out.println( a.stream().map(x ->
        // x.getSimpleName()).collect(Collectors.toList()));
        return a;
    }

    static boolean isBlank(CharSequence s) {
        return s == null || s.toString().trim().length() == 0;
    }

    @SafeVarargs
    static <T> T[] arr(T... a) {
        return a;
    }

    static int[] iarr(int... a) {
        return a;
    }

    static long[] larr(long... a) {
        return a;
    }

    static double[] darr(double... a) {
        return a;
    }

}
