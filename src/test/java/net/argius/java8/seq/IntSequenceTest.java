package net.argius.java8.seq;

import static net.argius.java8.seq.IntSequence.seq;
import static net.argius.java8.seq.TestUtils.iarr;
import static org.junit.Assert.*;
import java.util.*;
import java.util.stream.*;
import org.junit.*;

public class IntSequenceTest {

    static IntSequence seq0(int... a) {
        return new IntSequence() {

            @Override
            public int[] toArray() {
                return Arrays.copyOf(a, a.length);
            }

            @Override
            public int sum() {
                throw new UnsupportedOperationException();
            }

            @Override
            public IntSequence sortWith(int fromIndex, int toIndex, IntComparator cmp) {
                throw new UnsupportedOperationException();
            }

            @Override
            public int size() {
                return a.length;
            }

            @Override
            public OptionalInt min() {
                throw new UnsupportedOperationException();
            }

            @Override
            public OptionalInt max() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int at(int index) {
                throw new UnsupportedOperationException();
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
    public void testDistinct() {
        assertEquals(seq(8, 3, 2, 12, 18), seq(8, 3, 2, 12, 3, 8, 18).distinct());
    }

    @Test
    public void testEmpty() {
        IntSequence empty1 = IntSequence.empty();
        IntSequence empty2 = IntSequence.empty();
        assertEquals(seq(), empty1);
        assertSame(empty1, empty2);
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
    public void testHead() {
        assertEquals(4, seq(4, 23, 33, 1, 5, 19).head().getAsInt());
        assertTrue(seq(4).head().isPresent());
        assertFalse(seq().head().isPresent());
    }

    @Test
    public void testMap() {
        assertArrayEquals(iarr(24, 2, 6, 20), seq(23, 1, 5, 19).map(x -> x + 1).toArray());
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
    public void testRange() {
        // TODO do later
        // assertEquals(seq(1, 2, 3, 4), IntSequence.range(1, 4));
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
    public void testToArray() {
        // fail("Not yet implemented");
    }

}
