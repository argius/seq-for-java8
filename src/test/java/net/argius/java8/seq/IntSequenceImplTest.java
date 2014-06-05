package net.argius.java8.seq;

import static net.argius.java8.seq.IntSequence.seq;
import static net.argius.java8.seq.TestUtils.iarr;
import static org.junit.Assert.*;
import java.util.*;
import java.util.stream.*;
import org.junit.*;

public class IntSequenceImplTest {

    static IntSequenceImpl seq0(int... a) {
        return (IntSequenceImpl)IntSequenceFactory.createWithCopy(a);
    }

    @Test
    public void testAt() {
        // fail("Not yet implemented");
    }

    @Test
    public void testCount() {
        // fail("Not yet implemented");
    }

    @Test
    public void testEqualsObject() {
        IntSequence seq1 = seq(11, 22);
        IntSequence seq2 = seq(11, 22);
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
        assertEquals(seq(2, 8, 6), seq(3, 2, 8, 5, 6).filter(x -> x % 2 == 0));
    }

    @Test
    public void testFold() {
        // fail("Not yet implemented");
    }

    @Test
    public void testHashCode() {
        assertEquals(2347, seq(11, 22).hashCode());
        assertEquals(1034, seq(11).hashCode());
        assertEquals(962, seq().hashCode());
    }

    @Test
    public void testMapToObj() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMax() {
        assertEquals(33, seq(4, 23, 33, 1, 5, 19).max().getAsInt());
        assertEquals(343, seq(-1, 3, 8, 343, -53, 134).max().getAsInt());
        assertTrue(seq(-1).max().isPresent());
        assertFalse(seq().max().isPresent());
    }

    @Test
    public void testMin() {
        assertEquals(1, seq(4, 23, 33, 1, 5, 19).min().getAsInt());
        assertEquals(-53, seq(-1, 3, 8, 343, -53, 134).min().getAsInt());
        assertTrue(seq(-1).min().isPresent());
        assertFalse(seq().min().isPresent());
    }

    @Test
    public void testOfCollectionOfInteger() {
        // TODO
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            List<Integer> initValueList = IntStream.of(initValues).boxed().collect(Collectors.toList());
            IntSequence expected = new IntSequenceImpl(initValues);
            IntSequence actual1 = IntSequence.of(initValueList);
            IntSequence actual2 = IntSequence.of(initValueList);
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    // @Test
    // public void testHead() {
    // assertEquals(4, seq(4, 23, 33, 1, 5, 19).head().getAsInt());
    // }
    //
    // @Test
    // public void testTail() {
    // assertArrayEquals(seq(34, 1, 5, 19).toArray(), seq(23, 34, 1, 5,
    // 19).tail().toArray());
    // }

    @Test
    public void testOfIntArray() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            IntSequence expected = new IntSequenceImpl(initValues);
            IntSequence actual1 = IntSequence.of(initValues);
            IntSequence actual2 = IntSequence.of(initValues);
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testOfIntStream() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            IntSequence expected = new IntSequenceImpl(initValues);
            IntSequence actual1 = IntSequence.of(IntStream.of(initValues));
            IntSequence actual2 = IntSequence.of(IntStream.of(initValues));
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testReverse() {
        int[] arg = iarr(134, -53, 343, 8, 3, -1);
        IntSequence expected = seq(-1, 3, 8, 343, -53, 134);
        assertEquals(expected, seq(arg).reverse());
    }

    @Test
    public void testSeqCollectionOfInteger() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqIntArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqIntStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
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
        assertEquals(434, seq(134, -53, 343, 8, 3, -1).sum());
        assertEquals(134, seq(134).sum());
        assertEquals(0, seq().sum());
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
