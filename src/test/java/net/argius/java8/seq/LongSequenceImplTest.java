package net.argius.java8.seq;

import static net.argius.java8.seq.LongSequence.seq;
import static net.argius.java8.seq.TestUtils.larr;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class LongSequenceImplTest {

    static LongSequenceImpl seq0(long... a) {
        return (LongSequenceImpl) LongSequenceFactory.createWithCopy(a);
    }

    @Test
    public final void benchSort() {
        long[] a = new long[10000];
        Arrays.fill(a, 1000, 5000, 8);
        Arrays.fill(a, 6000, 9000, 13);
        seq(a).sort();
    }

    @Test
    public final void benchSortWith() {
        long[] a = new long[1000];
        Arrays.fill(a, 100, 500, 8);
        Arrays.fill(a, 600, 900, 13);
        seq(a).sortWith(LongComparator.NATURAL);
    }

    @Test
    public void testAt() {
        assertEquals(2, seq(1, 2, 3).at(1));
    }

    @Test
    public void testEqualsObject() {
        LongSequence seq1 = seq(11, 22);
        LongSequence seq2 = seq(11, 22);
        assertEquals(seq1, seq1);
        assertEquals(seq1, seq2);
        assertEquals(seq1, seq(11, 22));
        assertNotEquals(seq1, null);
        assertNotEquals(seq1, "");
        assertNotEquals(seq1, seq0(11, 22, 33));
        assertNotEquals(seq1, seq0(11, 33));
    }

    @Test
    public void testFilter() {
        assertEquals(seq(6L), seq(1, 6, 2).filter(x -> x > 3));
    }

    @Test
    public void testMax() {
        assertEquals(33, seq(4, 23, 33, 1, 5, 19).max().getAsLong());
        assertEquals(343, seq(-1, 3, 8, 343, -53, 134).max().getAsLong());
        assertTrue(seq(-1).max().isPresent());
        assertFalse(seq().max().isPresent());
    }

    @Test
    public void testMin() {
        assertEquals(1L, seq(4, 23, 33, 1, 5, 19).min().getAsLong());
        assertEquals(-53L, seq(-1, 3, 8, 343, -53, 134).min().getAsLong());
        assertTrue(seq(-1L).min().isPresent());
        assertFalse(seq().min().isPresent());
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
        assertEquals(2347, seq(11, 22).hashCode());
        assertEquals(1034, seq(11).hashCode());
        assertEquals(962, seq().hashCode());
    }

    @Test
    public void testHead() {
        assertEquals(OptionalLong.of(4), seq(4, 23, 33, 1, 5, 19).head());
        assertEquals(OptionalLong.of(23), seq(23, 33, 1, 5, 19).head());
        assertEquals(OptionalLong.empty(), seq().head());
    }

    @Test
    public void testMap() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMapToObj() {
        assertEquals(Sequence.of("AB3CD", "AB5CD", "AB7CD"), seq(3, 5, 7).mapToObj(x -> "AB" + x + "CD"));
    }

    @Test
    public void testOfCollectionOfLong() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfLongArray() {
        assertArrayEquals(larr(4L, 12, 3, 25, 0, 1), LongSequenceImpl.seq(4L, 12, 3, 25, 0, 1).toArray());
    }

    @Test
    public void testOfLongStream() {
        // fail("Not yet implemented");
    }

    @Test
    public final void testReverse() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        LongSequence expected = seq(-1, 3, 8, 343, -53, 134);
        assertEquals(expected, seq(arg).reverse());
    }

    @Test
    public void testSeqCollectionOfLong() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqLongArray() {
        assertArrayEquals(larr(1, 2, 5, 8), seq(1, 2, 5, 8).toArray());
    }

    @Test
    public void testSeqLongStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
    }

    @Test
    public final void testSort() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        long[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(seq(expected), seq(arg).sort());
    }

    @Test
    public final void testSortWith() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        seq(arg).sort().reverse().equals(seq0(arg).sortWith(LongComparator.REVERSE));
        assertEquals(seq(2, 8).sort().reverse(), seq0(2, 8).sortWith(LongComparator.REVERSE));
    }

    @Test
    public void testStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSubSequence() {
        assertEquals(seq(1, 6, 2), seq(1, 6, 2).subSequence(0, 2));
        assertEquals(seq(1, 6, 2), seq(1, 6, 2).subSequence(0, 3));
        assertEquals(seq(6, 2), seq(1, 6, 2).subSequence(1, 2));
        assertEquals(seq(1, 6), seq(1, 6, 2).subSequence(0, 1));
        assertEquals(seq(), seq(1, 6, 2).subSequence(3, 4));
    }

    @Test
    public void testSum() {
        assertEquals(434L, seq(134L, -53, 343, 8, 3, -1).sum());
        assertEquals(134L, seq(134L).sum());
        assertEquals(0, seq().sum());
    }

    @Test
    public void testTail() {
        assertEquals(seq(34, 1, 5, 19), seq(23, 34, 1, 5, 19).tail());
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToString() {
        assertEquals("[11, 22]", seq(11, 22).toString());
        assertEquals("[11]", seq(11).toString());
        assertEquals("[]", seq().toString());
    }

}
