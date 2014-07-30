package net.argius.java8.seq;

import static org.junit.Assert.fail;
import java.lang.reflect.*;
import java.util.*;
import org.junit.*;

public final class MetaTest {

    static final String[] methodList1 = { "at", "concat", "distinct", "drop", "empty", "exists", "filter", "find",
            "fold", "forEach", "head", "indexWhere", "map", "of", "reduce", "reverse", "seq", "size", "sort",
            "sortWith", "stream", "subSequence", "tail", "take", "takeWhile", "toArray", };

    static final String[] methodListForNumber = { "average", "mapToObj", "max", "min", "sum", };
    static final String[] methodListExceptInt = { "mapToInt", };
    static final String[] methodListOnlyInt = { "random", "range", };
    static final String[] methodListExceptLong = { "mapToLong", };
    static final String[] methodListExceptDouble = { "contains", "indexOf", "mapToDouble", };

    static void checkMethods(Class<?> c, String[]... lists) {
        for (String[] list : lists) {
            assert Sequence.of(list).toSet().size() == list.length : "list not unique";
            for (final String methodName : list)
                if (!TestUtils.hasMethod(c, methodName))
                    fail(String.format("class %s does not have [%s] method", c.getSimpleName(), methodName));
        }
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
