package net.argius.java8.seq;

import static org.junit.Assert.*;
import org.junit.*;

public class IntComparatorTest {

    @Test
    public void testCompareTo() {
        // ignore
    }

    @Test
    public void testEq() {
        assertFalse(IntComparator.NATURAL.eq(3, 1));
        assertTrue(IntComparator.NATURAL.eq(3, 3));
        assertFalse(IntComparator.NATURAL.eq(1, 3));
        assertFalse(IntComparator.REVERSE.eq(3, 1));
        assertTrue(IntComparator.REVERSE.eq(3, 3));
        assertFalse(IntComparator.REVERSE.eq(1, 3));
    }

    @Test
    public void testGt() {
        assertTrue(IntComparator.NATURAL.gt(3, 1));
        assertFalse(IntComparator.NATURAL.gt(3, 3));
        assertFalse(IntComparator.NATURAL.gt(1, 3));
        assertFalse(IntComparator.REVERSE.gt(3, 1));
        assertFalse(IntComparator.REVERSE.gt(3, 3));
        assertTrue(IntComparator.REVERSE.gt(1, 3));
    }

    @Test
    public void testLt() {
        assertFalse(IntComparator.NATURAL.lt(3, 1));
        assertFalse(IntComparator.NATURAL.lt(3, 3));
        assertTrue(IntComparator.NATURAL.lt(1, 3));
        assertTrue(IntComparator.REVERSE.lt(3, 1));
        assertFalse(IntComparator.REVERSE.lt(3, 3));
        assertFalse(IntComparator.REVERSE.lt(1, 3));
    }

}
