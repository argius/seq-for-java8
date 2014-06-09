package net.argius.java8.seq;

import static org.junit.Assert.fail;
import java.lang.reflect.*;
import java.util.*;
import org.junit.*;

public final class MetaTest {

    static final String[] methodList1 = { "of", "seq", "empty", "size", "at", "subSequence", "filter", "head", "tail",
            "map", "forEach", "fold", "reduce", "distinct", "reverse", "sort", "sortWith", "concat", "stream",
            "toArray", };

    static final String[] methodListForNumber = { "max", "min", "sum", "mapToObj", };
    static final String[] methodListExceptInt = { "mapToInt", };
    static final String[] methodListExceptLong = { "mapToLong", };
    static final String[] methodListExceptDouble = { "mapToDouble", };

    static void checkMethods(Class<?> c, String[]... lists) {
        for (String[] list : lists)
            for (final String methodName : list)
                if (!TestUtils.hasMethod(c, methodName))
                    fail(String.format("class %s does not have [%s] method", c.getSimpleName(), methodName));
    }

    @Test
    public void testSequenceImpl() {
        Class<?> c = Sequence.class;
        checkMethods(c, methodList1);
        checkMethods(c, methodListExceptInt);
        checkMethods(c, methodListExceptLong);
        checkMethods(c, methodListExceptDouble);
    }

    @Test
    public void testIntSequenceImpl() {
        Class<?> c = IntSequence.class;
        checkMethods(c, methodList1);
        checkMethods(c, methodListForNumber);
        checkMethods(c, methodListExceptLong);
        checkMethods(c, methodListExceptDouble);
    }

    @Test
    public void testLongSequenceImpl() {
        Class<?> c = LongSequence.class;
        checkMethods(c, methodList1);
        checkMethods(c, methodListForNumber);
        checkMethods(c, methodListExceptInt);
        checkMethods(c, methodListExceptDouble);
    }

    @Test
    public void testDoubleSequenceImpl() {
        Class<?> c = DoubleSequence.class;
        checkMethods(c, methodList1);
        checkMethods(c, methodListForNumber);
        checkMethods(c, methodListExceptInt);
        checkMethods(c, methodListExceptLong);
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
