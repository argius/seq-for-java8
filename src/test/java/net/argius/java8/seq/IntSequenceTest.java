package net.argius.java8.seq;

import static net.argius.java8.seq.IntSequence.*;
import static net.argius.java8.seq.TestUtils.*;
import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import org.junit.*;

public class IntSequenceTest {

    static IntSequence seq0(int... a) {
        return new IntSequence() {

            @Override
            public int at(int index) {
                throw new UnsupportedOperationException();
            }

            @Override
            public OptionalInt max() {
                throw new UnsupportedOperationException();
            }

            @Override
            public OptionalInt min() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int product() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int size() {
                return a.length;
            }

            @Override
            public IntSequence sortWith(int fromIndex, int toIndex, IntComparator cmp) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int sum() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int[] toArray() {
                return Arrays.copyOf(a, a.length);
            }

        };
    }

    @Test
    public void testAt() {
        assertEquals(1, seq(4, 23, 33, 1, 5, 19).at(3));
        assertEquals(34, seq(23, 34, 1, 5, 19).at(1));
    }

    @Test
    public void testAverage() {
        assertEquals(17.4285714285714, seq(22, 40, 2, 35, 37, -5, -9).average(), 0.00001d);
    }

    @Test
    public void testConcat() {
        assertEquals(seq(8, 3, 2, 12, -3, 6, 18), IntSequence.seq(8, 3, 2).concat(seq(12), seq(-3, 6, 18)));
    }

    @Test
    public void testContains() {
        assertTrue(seq(32, 42, 28, 20, -2).contains(28));
        assertFalse(seq(15, 25, 10, -3, -13).contains(3));
    }

    @Test
    public void testDistinct() {
        assertEquals(seq(8, 3, 2, 12, 18), seq(8, 3, 2, 12, 3, 8, 18).distinct());
    }

    @Test
    public void testDrop() {
        assertEquals(seq(12, 3, 8, 18), seq(8, 3, 2, 12, 3, 8, 18).drop(3));
        assertEquals(seq(), seq(8, 3, 2, 12, 3, 8, 18).drop(12));
        assertEquals(seq(5, 3, 8, 18), seq(5, 3, 8, 18).drop(0));
        assertEquals(seq(), seq(8, 3, 2, 12, 3, 8, 18).drop(8));
    }

    @Test
    public void testEmpty() {
        IntSequence empty1 = IntSequence.empty();
        IntSequence empty2 = IntSequence.empty();
        assertEquals(seq(), empty1);
        assertSame(empty1, empty2);
    }

    @Test
    public void testExists() {
        assertTrue(seq(23, 8, 30, 16, 22, 36, 0, 51, 36).exists(x -> x > 0));
        assertFalse(seq(47, 13, -8, 2, 34, 20, 0, 6, 40).exists(x -> x < -10));
    }

    @Test
    public void testFilter() {
        assertEquals(seq(2, 8, 6), seq(3, 2, 8, 5, 6).filter(x -> x % 2 == 0));
    }

    @Test
    public void testFold() {
        assertEquals(11, seq(1, 2, 3).fold(5, (x, y) -> x + y));
        assertEquals(5, seq().fold(5, (x, y) -> x + y));
    }

    @Test
    public void testForEach() {
        StringBuilder sb = new StringBuilder();
        seq(3, 2, 8, 5, 6).forEach(x -> sb.append(":").append(x));
        assertEquals(":3:2:8:5:6", sb.toString());
    }

    @Test
    public void testGenerate() {
        assertEquals(seq(4, 4, 4), generate(3, () -> 4));
        AtomicInteger aint = new AtomicInteger(-3);
        assertEquals(seq(-3, -2, -1, 0, 1), generate(5, () -> aint.getAndAdd(1)));
    }

    @Test
    public void testHead() {
        assertEquals(4, seq(4, 23, 33, 1, 5, 19).head().getAsInt());
        assertTrue(seq(4).head().isPresent());
        assertFalse(seq().head().isPresent());
    }

    @Test
    public void testIndexOf() {
        assertEquals(3, seq(34, 20, 13, 46, 20, 7, 0, 3, -5).indexOf(46));
        assertEquals(-1, seq(49, 16, 25, 44, 27, -7, 41, 19, 51).indexOf(-6));
    }

    @Test
    public void testIndexWhere() {
        assertEquals(2, seq(41, 11, 17, 21, 48, 31, 11, 4, 42).indexWhere(x -> x >= 16 && x < 30));
        assertEquals(-1, seq(40, 3, 37, 27, 0, 50, 9, 22, 21).indexWhere(x -> x < 0));
    }

    @Test
    public void testMap() {
        assertArrayEquals(iarr(24, 2, 6, 20), seq(23, 1, 5, 19).map(x -> x + 1).toArray());
    }

    @Test
    public void testMapToDouble() {
        assertArrayEquals(darr(1.33d, -1.33d, 4.67d, 6.67d, 1.00d, 1.00d, 6.67d), //
            seq(4, -4, 14, 20, 3, 3, 20).mapToDouble(x -> x / 3d).toArray(), 0.01d);
    }

    @Test
    public void testMapToLong() {
        assertArrayEquals(larr(17L, 2L, 20L, 22L, -2L, 24L), seq(15, 0, 18, 20, -4, 22).mapToLong(x -> x + 2).toArray());
    }

    @Test
    public void testMapToObj() {
        assertArrayEquals(Sequence.of("A1", "A2", "A3").toArray(), seq(1, 2, 3).mapToObj(x -> "A" + x).toArray());
    }

    @Test
    public void testOfCollectionOfInteger() {
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
    public void testRandom() {
        for (int i = 0; i < 1000; i++) {
            final int size = 12;
            final int min = -2;
            final int max = 4;
            IntSequence seq = random(size, min, max);
            assertEquals(size, seq.size());
            assertEquals(0, seq.filter(x -> x < min || x > max).size());
        }
    }

    @Test
    public void testRange() {
        assertEquals(seq(2, 3, 4, 5, 6), IntSequence.range(2, 6));
        try {
            IntSequence.range(5, 4);
            fail("requires an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal range: 5 to 4", e.getMessage());
        }
        assertEquals(seq(-12, -9, -6, -3, 0, 3), IntSequence.range(-12, 3, 3));
        // with step
        assertEquals(seq(5, 3, 1, -1, -3), IntSequence.range(5, -4, -2));
        try {
            IntSequence.range(5, 4, 1);
            fail("requires an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal range: 5 to 4 step 1", e.getMessage());
        }
        try {
            IntSequence.range(4, 5, -1);
            fail("requires an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal range: 4 to 5 step -1", e.getMessage());
        }
        try {
            IntSequence.range(4, 5, 0);
            fail("requires an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("illegal range: 4 to 5 step 0", e.getMessage());
        }
    }

    @Test
    public void testReduce() {
        assertEquals(434, seq(-1, 3, 8, 343, -53, 134).reduce((x, y) -> x + y).getAsInt());
        assertEquals(437, seq(-1, 3, 8, 343, -53, 134).reduce(3, (x, y) -> x + y));
        assertEquals(OptionalInt.empty(), seq(0).tail().reduce((x, y) -> x + y));
        assertEquals(3, seq().reduce(3, (x, y) -> x + y));
    }

    @Test
    public void testReverse() {
        int[] arg = iarr(134, -53, 343, 8, 3, -1);
        IntSequence expected = seq(-1, 3, 8, 343, -53, 134);
        assertEquals(expected, seq(arg).reverse());
    }

    @Test
    public void testSeqCollectionOfInteger() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            List<Integer> initValueList = IntStream.of(initValues).boxed().collect(Collectors.toList());
            IntSequence expected = new IntSequenceImpl(initValues);
            IntSequence actual1 = IntSequence.seq(initValueList);
            IntSequence actual2 = IntSequence.seq(initValueList);
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testSeqIntArray() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            IntSequence expected = new IntSequenceImpl(initValues);
            IntSequence actual1 = IntSequence.seq(initValues);
            IntSequence actual2 = IntSequence.seq(initValues);
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testSeqIntStream() {
        int[][] arrayOfInitValues = { {}, { 1, 2 } };
        for (int[] initValues : arrayOfInitValues) {
            IntSequence expected = new IntSequenceImpl(initValues);
            IntSequence actual1 = IntSequence.seq(IntStream.of(initValues));
            IntSequence actual2 = IntSequence.seq(IntStream.of(initValues));
            assertNotSame(expected, actual1);
            assertNotSame(expected, actual2);
            assertNotSame(actual1, actual2);
            assertArrayEquals(expected.toArray(), actual1.toArray());
            assertArrayEquals(expected.toArray(), actual2.toArray());
        }
    }

    @Test
    public void testSize() {
        assertEquals(6, seq(4, 23, 33, 1, 5, 19).size());
        assertEquals(5, seq(23, 34, 1, 5, 19).size());
    }

    @Test
    public void testSort() {
        int[] arg = iarr(134, -53, 343, 8, 3, -1);
        int[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(seq(expected), seq(arg).sort());
    }

    @Test
    public void testSortWith() {
        assertEquals(seq(343, 134, 8, 3, -1, -53), seq(iarr(134, -53, 343, 8, 3, -1)).sortWith(IntComparator.REVERSE));
        assertEquals(seq(343, 134, 8, 3, -1, -53), seq(134, -53, 343, 8, 3, -1).sortWith(IntComparator.REVERSE));
        assertEquals(seq(20, 3), seq(3, 20).sortWith(IntComparator.REVERSE));
    }

    @Test
    public void testStream() {
        assertArrayEquals(iarr(16, 38, 48, 20, -5), seq0(16, 38, 48, 20, -5).stream().toArray());
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
    public void testTail() {
        assertArrayEquals(seq(34, 1, 5, 19).toArray(), seq(23, 34, 1, 5, 19).tail().toArray());
    }

    @Test
    public void testTake() {
        assertEquals(seq(23, 34, 1), seq(23, 34, 1, 5, 19).take(3));
        assertEquals(seq(), seq(23, 34, 1, 5, 19).take(0));
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

}
