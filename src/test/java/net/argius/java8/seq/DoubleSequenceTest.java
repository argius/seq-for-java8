package net.argius.java8.seq;

import static net.argius.java8.seq.DoubleSequence.*;
import static net.argius.java8.seq.TestUtils.*;
import static org.junit.Assert.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import org.junit.*;

public class DoubleSequenceTest {

    private static final double DELTA = 0.00001d;

    @Test
    public void testAt() {
        // fail("Not yet implemented");
    }

    @Test
    public void testAverage() {
        assertEquals(28.52571d, seq(42.08d, 27.37d, 36.63d, 41.03d, 12.11d, 50.10d, -9.64d).average(), 0.001d);
    }

    @Test
    public void testConcat() {
        assertEquals(seq(15.3d, -4.2, 0.3, 3.3d, -0.5, 2.3, -1.5),
            seq(15.3d, -4.2, 0.3).concat(seq(3.3d, -0.5), seq(2.3, -1.5)));
    }

    @Test
    public void testDistinct() {
        assertEquals(seq(8d, 3, 2, 12, 18), seq(8d, 3, 2, 12, 3, 8, 18).distinct());
    }

    @Test
    public void testDrop() {
        assertEquals(seq(-1.34d, 1.92d, 29.95d), seq(14.59d, 24.80d, 34.88d, -1.34d, 1.92d, 29.95d).drop(3));
        assertEquals(seq(), seq(14.59d, 24.80d, 34.88d, -1.34d, 1.92d, 29.95d).drop(8));
        assertEquals(seq(), seq(32.52d, 30.51d, -5.33d, 7.60d, 46.04d, 25.82d).drop(16));
        assertEquals(seq(), seq().drop(3));
    }

    @Test
    public void testEmpty() {
        assertEquals(seq(), DoubleSequence.empty());
    }

    @Test
    public void testEqualsObject() {
        DoubleSequence seq1 = seq(1.1, 2.2);
        DoubleSequence seq2 = seq(1.1, 2.2);
        assertEquals(seq1, seq1);
        assertEquals(seq1, seq2);
        assertEquals(seq1, seq(1.1, 2.2));
    }

    @Test
    public void testExists() {
        assertTrue(seq(50.11d, 46.33d, 22.36d, -10.62d, 46.58d, 10.29d, 17.21d, -4.43d, 13.62d).exists(x -> x < 10.6d));
        assertFalse(seq(-0.61d, 21.05d, 18.48d, 14.65d, 18.69d, -10.12d, -8.16d, 3.89d, 19.61d).exists(x -> x == 18.5d));
    }

    @Test
    public void testFilter() {
        assertEquals(seq(8.1d, 12, 8, 18), seq(8.1d, 3.6, 2, 12, -3, 8, 18).filter(x -> x > 5));
    }

    @Test
    public void testFold() {
        assertEquals(435.6d, seq(134.3d, -53, 343, 8, 3, -1).fold(1.3d, (x, y) -> x + y), DELTA);
        assertEquals(-52.1d, seq(-53.8d).fold(1.7d, (x, y) -> x + y), DELTA);
        assertEquals(-3.1d, seq().fold(-3.1d, (x, y) -> x + y), DELTA);
    }

    @Test
    public void testForEach() {
        StringBuilder sb = new StringBuilder();
        seq().forEach(x -> {
            sb.append(":").append(x);
        });
        assertEquals("", sb.toString());
        seq(4.3d, 8, 2, -3.1).forEach(x -> {
            sb.append(":").append(x);
        });
        assertEquals(":4.3:8.0:2.0:-3.1", sb.toString());
    }

    @Test
    public void testGenerate() {
        assertEquals(seq(0.81d, 0.81d, 0.81d), generate(3, () -> 0.81d));
        DoubleAdder dadder = new DoubleAdder();
        assertEquals(seq(0.57d, 1.14d, 1.71d, 2.28d), generate(4, () -> {
            dadder.add(0.57d);
            return dadder.sum();
        }));
    }

    @Test
    public void testHashCode() {
        assertEquals(3147808, seq(1.1, 2.2).hashCode());
        assertEquals(-1503132670, seq(1.1).hashCode());
        assertEquals(962, seq().hashCode());
    }

    @Test
    public void testHead() {
        assertEquals(OptionalDouble.of(8.2d), seq(8.2d, 3, 12, -8).head());
        assertEquals(OptionalDouble.empty(), seq().head());
    }

    @Test
    public void testIndexWhere() {
        assertEquals(6,
            seq(7.74d, 35.33d, -1.04d, 14.77d, -10.91d, -7.21d, 25.87d, 8.36d, 49.55d).indexWhere(x -> ((int)x) == 25));
        assertEquals(-1,
            seq(51.02d, 9.25d, 49.79d, 3.56d, -0.32d, 49.38d, 29.28d, 39.90d, 9.53d).indexWhere(x -> x < -1));
    }

    @Test
    public void testMap() {
        assertArrayEquals(seq(4.0d, 1.5d, 6d, -4d).toArray(), seq(8d, 3, 12, -8).map(x -> x / 2).toArray(), DELTA);
    }

    @Test
    public void testMapToInt() {
        assertArrayEquals(iarr(36, -20, 70, 27, 49, -12, 37, 0),
            seq(9.07d, -5.08d, 17.73d, 6.97d, 12.40d, -3.05d, 9.38d, 0.0d).mapToInt(x -> (int)(x * 4)).toArray());
    }

    @Test
    public void testMapToLong() {
        assertArrayEquals(larr(19L, 66L, -12L, -4L, 23L, 59L, 0L),
            seq(6.58d, 22.14d, -4.05d, -1.58d, 7.98d, 19.89d, -0.24d).mapToLong(x -> (long)(x * 3)).toArray());
    }

    @Test
    public void testMapToObj() {
        assertEquals(Sequence.of("43.5%", "100.0%", "8.0%"),
            seq(0.435d, 1.0d, 0.08d).mapToObj(x -> String.format("%.1f%%", x * 100)));
    }

    @Test
    public void testOfCollectionOfDouble() {
        assertEquals(seq(47.21d, 40.93d, 7.59d, 11.57d, -10.33d, 44.00d, 37.23d),
            of(Arrays.asList(47.21d, 40.93d, 7.59d, 11.57d, -10.33d, 44.00d, 37.23d)));
    }

    @Test
    public void testOfDoubleArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfDoubleStream() {
        assertEquals(seq(0.37d, 24.98d, 9.46d, 19.55d, 28.90d, -11.07d, 2.75d),
            of(DoubleStream.of(0.37d, 24.98d, 9.46d, 19.55d, 28.90d, -11.07d, 2.75d)));
    }

    @Test
    public void testRandom() {
        for (int i = 0; i < 1000; i++) {
            final int size = 14;
            final double min = -1.2d;
            final double max = 3.6d;
            DoubleSequence seq = random(size, min, max);
            assertEquals(size, seq.size());
            assertEquals(0, seq.filter(x -> x < min || x > max).size());
        }
    }

    @Test
    public void testReduce() {
        assertEquals(434.3d, seq(134.3d, -53, 343, 8, 3, -1).reduce((x, y) -> x + y).getAsDouble(), DELTA);
        assertEquals(435.6d, seq(134.3d, -53, 343, 8, 3, -1).reduce(1.3d, (x, y) -> x + y), DELTA);
        assertEquals(OptionalDouble.empty(), seq(-1d).tail().reduce((x, y) -> x + y));
        assertEquals(1.3d, seq().reduce(1.3d, (x, y) -> x + y), DELTA);
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
        assertEquals(seq(38.19d, 17.03d, 28.75d, 31.61d, 49.02d, 26.22d, -5.20d),
            seq(Arrays.asList(38.19d, 17.03d, 28.75d, 31.61d, 49.02d, 26.22d, -5.20d)));
    }

    @Test
    public void testSeqDoubleArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqDoubleStream() {
        assertEquals(seq(-6.35d, 11.46d, 8.02d, 25.54d, 29.09d, 44.87d, 41.05d),
            seq(DoubleStream.of(-6.35d, 11.46d, 8.02d, 25.54d, 29.09d, 44.87d, 41.05d)));
    }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSort() {
        double[] arg = darr(134, -53, 343, 8, 3, -1);
        double[] expected = Arrays.copyOf(arg, arg.length);
        Arrays.sort(expected);
        assertEquals(seq(expected), seq(arg).sort());
    }

    @Test
    public void testStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSubSequence() {
        assertEquals(seq(-53, 343, 8, 3), seq(134, -53, 343, 8, 3, -1).subSequence(1, 4));
    }

    @Test
    public void testTail() {
        // fail("Not yet implemented");
    }

    @Test
    public void testTake() {
        assertEquals(seq(134.2d, -53, 343), seq(134.2d, -53, 343, 8, 3, -1).take(3));
        assertEquals(seq(), seq(0.4d, 23.1, 31.3).take(0));
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
