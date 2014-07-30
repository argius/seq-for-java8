package net.argius.java8.seq;

import static net.argius.java8.seq.LongSequence.*;
import static net.argius.java8.seq.TestUtils.*;
import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import org.apache.commons.lang3.*;
import org.junit.*;

public class LongSequenceTest {

    static LongSequence seq0(long... a) {
        return new LongSequence() {

            @Override
            public long at(int index) {
                throw new UnsupportedOperationException();
            }

            @Override
            public OptionalLong max() {
                throw new UnsupportedOperationException();
            }

            @Override
            public OptionalLong min() {
                throw new UnsupportedOperationException();
            }

            @Override
            public long product() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int size() {
                return a.length;
            }

            @Override
            public LongSequence sortWith(int fromIndex, int toIndex, LongComparator cmp) {
                throw new UnsupportedOperationException();
            }

            @Override
            public long sum() {
                throw new UnsupportedOperationException();
            }

            @Override
            public long[] toArray() {
                return Arrays.copyOf(a, a.length);
            }

        };
    }

    @Test
    public void testAverage() {
        assertEquals(20.1428571428571d, seq(20L, 3L, 42L, 47L, 15L, -9L, 23L).average(), 0.00001d);
    }

    @Test
    public void testConcat() {
        assertEquals(seq(8L, 3, 2, 12, -3, 6, 18), seq(8L, 3, 2).concat(seq(12L), seq(-3L, 6, 18)));
    }

    @Test
    public void testContains() {
        assertTrue(seq(39L, 33L, 29L, 24L, 32L, 48L, 27L, 30L, 38L).contains(33L));
        assertFalse(seq(9L, 47L, -1L, -8L, 45L, 34L, 13L, -2L, 45L).contains(46L));
    }

    @Test
    public void testDistinct() {
        assertEquals(seq(8, 3, 2, 12, 18), seq(8, 3, 2, 12, 3, 8, 18).distinct());
    }

    @Test
    public void testDrop() {
        assertEquals(seq(15L, -9L, 23L), seq(20L, 3L, 42L, 47L, 15L, -9L, 23L).drop(4));
        assertEquals(seq(7L, 15L, -9L, 23L), seq(7L, 15L, -9L, 23L).drop(0));
        assertEquals(seq(), seq(7L, 15L, -9L, 23L).drop(8));
        assertEquals(seq(), seq().drop(1));
    }

    @Test
    public void testEmpty() {
        assertEquals(seq(), LongSequence.empty());
    }

    @Test
    public void testExists() {
        assertTrue(seq(27L, 31L, 10L, 0L, 11L, 41L, 34L, -4L, 0L).exists(x -> x == 10));
        assertFalse(seq(16L, 8L, -2L, 5L, 36L, -8L, 39L, 5L, 38L).exists(x -> x == 1));
    }

    @Test
    public void testFilter() {
        assertEquals(seq(6L), seq(1, 6, 2).filter(x -> x > 3));
    }

    @Test
    public void testFind() {
        assertEquals(OptionalLong.of(7L), seq(-9L, 7L, -3L, 18L, 3L, 19L, 12L).find(x -> x > 1L));
        assertEquals(OptionalLong.of(-6L), seq(8L, -7L, 0L, 3L, 13L, -6L, -19L).find(x -> x % 2 == 0, 3));
        assertEquals(OptionalLong.empty(), seq(-15L, -11L, 17L, -19L, -13L, 5L, 7L).find(x -> x % 2 == 0, 3));
    }

    @Test
    public void testFold() {
        assertEquals(12L, seq(1, 6, 2).fold(3, (x, y) -> x + y));
        assertEquals(3L, seq().fold(3, (x, y) -> x + y));
    }

    @Test
    public void testForEach() {
        StringBuilder sb = new StringBuilder();
        seq(3L, 5, 8).forEach(x -> {
            sb.append("--").append(x);
        });
        assertEquals("--3--5--8", sb.toString());
    }

    @Test
    public void testGenerate() {
        assertEquals(seq(7L, 7, 7), generate(3, () -> 7L));
        AtomicLong along = new AtomicLong(-5L);
        assertEquals(seq(-5L, -4, -3, -2, -1), generate(5, () -> along.getAndAdd(1)));
    }

    @Test
    public void testHead() {
        assertEquals(OptionalLong.of(4), seq(4, 23, 33, 1, 5, 19).head());
        assertEquals(OptionalLong.of(23), seq(23, 33, 1, 5, 19).head());
        assertEquals(OptionalLong.empty(), seq().head());
    }

    @Test
    public void testIndexOf() {
        assertEquals(5, seq(14L, -9L, 10L, 26L, -4L, 30L, 0L, 24L, 5L).indexOf(30L));
        assertEquals(-1, seq(16L, 2L, 48L, 51L, 5L, -7L, 5L, 50L, -6L).indexOf(3));
    }

    @Test
    public void testIndexWhere() {
        assertEquals(1, seq(15L, -6L, -8L, -5L, 8L, -9L, 36L, 4L, 24L).indexWhere(x -> x < 0L));
        assertEquals(-1, seq(10L, 15L, 39L, 23L, 27L, 33L, 23L, -3L, -2L).indexWhere(x -> x < -3L));
    }

    @Test
    public void testMap() {
        assertEquals(seq(26, 37, 4, 8, 22), seq(23, 34, 1, 5, 19).map(x -> x + 3));
    }

    @Test
    public void testMapToDouble() {
        assertArrayEquals(darr(2.857d, 3.143d, 0.714d, 0.143d, 0.143d, 1.429d), //
            seq(20L, 22L, 5L, 1L, 1L, 10L).mapToDouble(x -> x / 7d).toArray(), 0.001d);
    }

    @Test
    public void testMapToInt() {
        assertArrayEquals(iarr(36, -6, 42, -24, 60, -6), //
            seq(12L, -2L, 14L, -8L, 20L, -2L).mapToInt(x -> (int)(x * 3)).toArray());
    }

    @Test
    public void testMapToObj() {
        assertEquals(Sequence.of("AB3CD", "AB5CD", "AB7CD"), seq(3, 5, 7).mapToObj(x -> "AB" + x + "CD"));
    }

    @Test
    public void testOfCollectionOfLong() {
        assertEquals(seq(44L, -9L, 18L, 39L, 51L, 19L), of(Arrays.asList(44L, -9L, 18L, 39L, 51L, 19L)));
    }

    @Test
    public void testOfLongArray() {
        assertEquals(seq(1, 2, 3), of(1, 2, 3));
    }

    @Test
    public void testOfLongStream() {
        assertEquals(seq(48L, -5L, 35L, 43L, -2L, 7L), of(LongStream.of(48L, -5L, 35L, 43L, -2L, 7L)));
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 1000; i++) {
            final int size = 13;
            final long min = -8L;
            final long max = 12L;
            LongSequence seq = random(size, min, max);
            assertEquals(size, seq.size());
            assertEquals(0, seq.filter(x -> x < min || x > max).size());
        }
    }

    @Test
    public void testReduce() {
        assertEquals(105L, seq(3, 5, 7).reduce((x, y) -> x * y).getAsLong());
        assertEquals(420L, seq(3, 5, 7).reduce(4L, (x, y) -> x * y));
        assertEquals(OptionalLong.empty(), seq(0L).tail().reduce((x, y) -> x * y));
        assertEquals(4L, seq().reduce(4L, (x, y) -> x * y));
    }

    @Test
    public final void testReverse() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        long[] expected0 = Arrays.copyOf(arg, arg.length);
        ArrayUtils.reverse(expected0);
        LongSequence expected = seq(expected0);
        assertEquals(expected, seq(arg).reverse());
    }

    @Test
    public void testSeqCollectionOfLong() {
        assertEquals(of(38L, 47L, 8L, 33L, 35L, 24L), seq(Arrays.asList(38L, 47L, 8L, 33L, 35L, 24L)));
    }

    @Test
    public void testSeqLongArray() {
        assertEquals(of(20L, 51L, 0L, 3L, 50L, -6L), seq(20L, 51L, 0L, 3L, 50L, -6L));
    }

    @Test
    public void testSeqLongStream() {
        assertEquals(of(-11L, 31L, 5L, 9L, -2L, 40L), seq(LongStream.of(-11L, 31L, 5L, 9L, -2L, 40L)));
    }

    @Test
    public final void testSort() {
        long[] arg = larr(134, -53, 343, 8, 3, -1);
        long[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(seq(expected), seq0(arg).sort());
    }

    @Test
    public void testStream() {
        final LongSequence a = seq0(2, 3, 4);
        @SuppressWarnings("resource")
        LongStream st = a.stream();
        assertEquals(OptionalLong.of(24), st.reduce((x, y) -> x * y));
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
        assertEquals(seq(34, 1, 5, 19), seq(23, 34, 1, 5, 19).tail());
    }

    @Test
    public void testTake() {
        assertEquals(seq(23L, 34, 1), seq(23L, 34, 1, 5, 19).take(3));
        assertEquals(seq(), seq(23L, 34, 1, 5, 19).take(0));
    }

    @Test
    public void testTakeWhile() {
        assertEquals(seq(23L, 34), seq(23L, 34, 1, 5, 19).takeWhile(x -> x > 3));
        assertEquals(seq(23L), seq(23L, 34, 1, 5, 19).takeWhile(x -> x < 30));
        assertEquals(seq(), seq(23L, 34, 1, 5, 19).takeWhile(x -> x < 3));
    }

    @Test
    public void testToArray() {
        // fail("Not yet implemented");
    }

}
