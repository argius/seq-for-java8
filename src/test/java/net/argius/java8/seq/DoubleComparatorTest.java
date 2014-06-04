package net.argius.java8.seq;

import static org.junit.Assert.*;
import org.junit.*;

public class DoubleComparatorTest {

    @Test
    public void testCompareTo() {
        // ignore
    }

    @Test
    public void testEq() {
        assertFalse(DoubleComparator.NATURAL.eq(3, 1));
        assertTrue(DoubleComparator.NATURAL.eq(3, 3));
        assertFalse(DoubleComparator.NATURAL.eq(1, 3));
        assertFalse(DoubleComparator.REVERSE.eq(3, 1));
        assertTrue(DoubleComparator.REVERSE.eq(3, 3));
        assertFalse(DoubleComparator.REVERSE.eq(1, 3));
    }

    @Test
    public void testGt() {
        assertTrue(DoubleComparator.NATURAL.gt(3, 1));
        assertFalse(DoubleComparator.NATURAL.gt(3, 3));
        assertFalse(DoubleComparator.NATURAL.gt(1, 3));
        assertFalse(DoubleComparator.REVERSE.gt(3, 1));
        assertFalse(DoubleComparator.REVERSE.gt(3, 3));
        assertTrue(DoubleComparator.REVERSE.gt(1, 3));
    }

    @Test
    public void testLt() {
        assertFalse(DoubleComparator.NATURAL.lt(3, 1));
        assertFalse(DoubleComparator.NATURAL.lt(3, 3));
        assertTrue(DoubleComparator.NATURAL.lt(1, 3));
        assertTrue(DoubleComparator.REVERSE.lt(3, 1));
        assertFalse(DoubleComparator.REVERSE.lt(3, 3));
        assertFalse(DoubleComparator.REVERSE.lt(1, 3));
    }

}
