package net.argius.java8.seq;

import static org.junit.Assert.fail;
import java.lang.reflect.*;
import java.util.*;
import org.junit.*;

public final class MetaTest {

    // TODO add unimplemented methods
    static final String[] methodList1 = { "of", "seq", "empty", "size", "at", "subSequence", "filter", "head", "tail",
            "map", "forEach", "fold", "reduce", "distinct", "reverse", "sort", "sortWith", "concat", "stream",
            "toArray", "toString", };
    // all, any,

    static final String[] methodListForNumber = { "max", "min", "sum", };

    @Test
    public void testSequenceImpl() {
        checkMethods(SequenceImpl.class, methodList1);
    }

    static void checkMethods(Class<?> c, String[]... lists) {
        for (String[] list : lists)
            for (final String methodName : list)
                if (!TestUtils.hasMethod(c, methodName))
                    fail(String.format("class %s does not have [%s] method", c.getSimpleName(), methodName));
    }

    @Test
    public void testIntSequenceImpl() {
        checkMethods(IntSequenceImpl.class, methodList1);
        checkMethods(IntSequenceImpl.class, methodListForNumber);
    }

    @Test
    public void testLongSequenceImpl() {
        checkMethods(LongSequenceImpl.class, methodList1);
        checkMethods(LongSequenceImpl.class, methodListForNumber);
    }

    @Test
    public void testDoubleSequenceImpl() {
        checkMethods(DoubleSequenceImpl.class, methodList1);
        checkMethods(DoubleSequenceImpl.class, methodListForNumber);
    }

    @Test
    public void testPrivateCtors() throws Exception {
        for (Class<?> c : Arrays.asList(SequenceFactory.class, IntSequenceFactory.class, LongSequenceFactory.class,
            DoubleSequenceFactory.class)) {
            Constructor<?> ctor = c.getDeclaredConstructor();
            ctor.setAccessible(true);
            ctor.newInstance();
        }
    }

}
