package net.argius.java8.seq;

import static net.argius.java8.seq.DoubleSequence.seq;
import static net.argius.java8.seq.TestUtils.darr;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class DoubleSequenceImplTest {

    private static final double DELTA = 0.00001d;

    static DoubleSequenceImpl seq0(double... a) {
        return (DoubleSequenceImpl) DoubleSequenceFactory.createWithCopy(a);
    }

    @Test
    public void testAt() {
        assertEquals(2.2d, seq0(1.1d, 2.2, 3.3).at(1), 0.1d);
    }

    @Test
    public void testEqualsObject() {
        DoubleSequence seq1 = seq(1.1d, 2.2);
        DoubleSequence seq2 = seq(1.1d, 2.2);
        assertEquals(seq1, seq1);
        assertEquals(seq1, seq2);
        assertEquals(seq1, seq(1.1, 2.2));
        assertNotEquals(seq1, null);
        assertNotEquals(seq1, "");
        assertNotEquals(seq1, seq0(11, 22, 33));
        assertNotEquals(seq1, seq0(11, 33));
    }

    @Test
    public void testFilter() {
        // fail("Not yet implemented");
    }

    @Test
    public void testFold() {
        // fail("Not yet implemented");
    }

    @Test
    public void testForEach() {
        // fail("Not yet implemented");
    }

    @Test
    public void testHashCode() {
        assertEquals(3147808, seq(1.1, 2.2).hashCode());
        assertEquals(-1503132670, seq(1.1).hashCode());
        assertEquals(962, seq().hashCode());
    }

    @Test
    public void testHead() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMap() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMapToObj() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMax() {
        assertEquals(33.1, seq(4d, 23, 33.1, 1, 5, 19).max().getAsDouble(), DELTA);
        assertEquals(343.1, seq(-1d, 3, 8, 343.1, -53, 134).max().getAsDouble(), DELTA);
        assertTrue(seq(-1).max().isPresent());
        assertFalse(seq().max().isPresent());
    }

    @Test
    public void testMin() {
        assertEquals(1d, seq(4d, 23, 33, 1, 5, 19).min().getAsDouble(), DELTA);
        assertEquals(-53d, seq(-1d, 3, 8, 343, -53, 134).min().getAsDouble(), DELTA);
        assertTrue(seq(-1f).min().isPresent());
        assertFalse(seq().min().isPresent());
    }

    @Test
    public void testOfCollectionOfDouble() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfDoubleArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfDoubleStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testReverse() {
        assertEquals(seq(-1, 3, 8, 343, -53, 134), seq(134, -53, 343, 8, 3, -1).reverse());
        assertEquals(seq(-1, 3), seq(3, -1).reverse());
        assertEquals(seq(-1), seq(-1).reverse());
        assertEquals(seq(), seq().reverse());
    }

    @Test
    public void testSeqCollectionOfDouble() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqDoubleArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqDoubleStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSize() {
        assertEquals(3, seq0(1.1, 2.2, 3.3).size());
    }

    @Test
    public void testSort() {
        double[] arg = darr(134, -53, 343, 8, 3, -1);
        double[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(seq(expected), seq(arg).sort());
    }

    @Test
    public void testSortWith() {
        assertEquals(seq(343, 134, 8, 3, -1, -53), seq(134d, -53, 343, 8, 3, -1).sortWith(DoubleComparator.REVERSE));
        assertEquals(seq(134, -53), seq(-53, 134d).sortWith(DoubleComparator.REVERSE));
    }

    @Test
    public void testStream() {
        // TODO later
        assertEquals(1, seq0(1).stream().count());
    }

    @Test
    public void testSubSequence() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSum() {
        assertEquals(6.6d, seq0(1.1d, 2.2, 3.3).sum(), DELTA);
        assertEquals(1.1d, seq0(1.1f).sum(), DELTA);
        assertEquals(0d, seq0().sum(), DELTA);
    }

    @Test
    public void testTail() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToString() {
        assertEquals("[1.1, 2.2]", seq(1.1, 2.2).toString());
        assertEquals("[1.1]", seq(1.1).toString());
        assertEquals("[]", seq().toString());
    }

}
