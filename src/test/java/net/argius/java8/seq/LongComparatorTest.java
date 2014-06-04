package net.argius.java8.seq;

import static org.junit.Assert.*;
import org.junit.*;

public class LongComparatorTest {

    @Test
    public void testCompareTo() {
        // ignore
    }

    @Test
    public void testEq() {
        assertFalse(LongComparator.NATURAL.eq(3, 1));
        assertTrue(LongComparator.NATURAL.eq(3, 3));
        assertFalse(LongComparator.NATURAL.eq(1, 3));
        assertFalse(LongComparator.REVERSE.eq(3, 1));
        assertTrue(LongComparator.REVERSE.eq(3, 3));
        assertFalse(LongComparator.REVERSE.eq(1, 3));
    }

    @Test
    public void testGt() {
        assertTrue(LongComparator.NATURAL.gt(3, 1));
        assertFalse(LongComparator.NATURAL.gt(3, 3));
        assertFalse(LongComparator.NATURAL.gt(1, 3));
        assertFalse(LongComparator.REVERSE.gt(3, 1));
        assertFalse(LongComparator.REVERSE.gt(3, 3));
        assertTrue(LongComparator.REVERSE.gt(1, 3));
    }

    @Test
    public void testLt() {
        assertFalse(LongComparator.NATURAL.lt(3, 1));
        assertFalse(LongComparator.NATURAL.lt(3, 3));
        assertTrue(LongComparator.NATURAL.lt(1, 3));
        assertTrue(LongComparator.REVERSE.lt(3, 1));
        assertFalse(LongComparator.REVERSE.lt(3, 3));
        assertFalse(LongComparator.REVERSE.lt(1, 3));
    }

}
