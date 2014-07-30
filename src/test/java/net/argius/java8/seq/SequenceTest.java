package net.argius.java8.seq;

import static net.argius.java8.seq.Sequence.seq;
import static net.argius.java8.seq.TestUtils.arr;
import static org.junit.Assert.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.junit.*;

public final class SequenceTest {

    static final class SequenceImpl0<T> implements Sequence<T> {

        int size;
        private T[] values;

        @SafeVarargs
        SequenceImpl0(T... a) {
            this.size = a.length;
            this.values = a;
        }

        @Override
        public T at(int index) {
            return values[index];
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SequenceImpl0<?> other = (SequenceImpl0<?>)obj;
            if (size != other.size)
                return false;
            if (!Arrays.equals(values, other.values))
                return false;
            return true;
        }

        @Override
        public Sequence<T> filter(Predicate<? super T> predicate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + size;
            result = prime * result + Arrays.hashCode(values);
            return result;
        }

        @Override
        public Iterator<T> iterator() {
            return new IteratorImpl();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public T[] toArray() {
            return Arrays.copyOf(values, size);
        }

        @Override
        public String toString() {
            return Arrays.toString(values);
        }

        final class IteratorImpl implements Iterator<T> {

            private int p;

            IteratorImpl() {
                this.p = -1;
            }

            @Override
            public boolean hasNext() {
                if (p + 1 < size) {
                    ++p;
                    return true;
                }
                else
                    return false;
            }

            @Override
            public T next() {
                return at(p);
            }

        }

    }

    @SafeVarargs
    static <T> Sequence<T> seq0(T... a) {
        return new SequenceImpl0<>(Arrays.copyOf(a, a.length));
    }

    @Test
    public void testAt() {
        // fail("Not yet implemented");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testConcat() {
        StringBuilder sb = new StringBuilder("X");
        assertEquals(seq("a", "b", "c", "d", "e"), seq("a", "b").concat(seq("c"), seq("d", "e")));
        assertEquals(seq(sb, "b", "c", "d", "e"), seq(sb, "b").concat(seq("c"), seq("d", "e")));
        // Stream.concat(a, b);
    }

    @Test
    public void testContains() {
        assertTrue(seq("java", "scala", "perl", "ruby", "python").contains("perl"));
        assertFalse(seq("java", "scala", "perl", "ruby", "python").contains("haskell"));
        assertFalse(seq().contains("java"));
    }

    @Test
    public void testDistinct() {
        String[] a = arr("java", "scala", "perl", "java", "python");
        assertEquals(seq("java", "scala", "perl", "python"), seq0(a).distinct());
    }

    @Test
    public void testDrop() {
        assertEquals(seq("perl", "ruby", "python"), seq("java", "scala", "perl", "ruby", "python").drop(2));
        assertEquals(seq(), seq("java", "scala", "perl", "ruby", "python").drop(8));
        assertEquals(seq(), seq().drop(1));
    }

    @Test
    public void testExists() {
        assertTrue(seq("java", "scala", "perl", "ruby", "python").exists(x -> x.length() == 6));
        assertFalse(seq("java", "scala", "perl", "ruby", "python").exists(x -> x.length() == 7));
        assertFalse(seq("").tail().exists(x -> x.isEmpty()));
    }

    @Test
    public void testFilter() {
        Sequence<String> seq = seq("java", "scala", "perl", "ruby", "python");
        assertEquals(seq("java", "perl", "ruby"), seq.filter(x -> x.length() == 4));
    }

    @Test
    public void testFind() {
        Sequence<String> seq = seq("java", "scala", "perl", "ruby", "python");
        assertEquals(Optional.of("scala"), seq.find(x -> x.length() == 5));
        assertEquals(Optional.of("python"), seq.find(x -> x.length() > 4, 2));
        assertEquals(Optional.empty(), seq.find(x -> x.endsWith("a"), 2));
    }

    @Test
    public void testFold() {
        assertEquals("FACE", seq("A", "C", "E").fold("F", (x, y) -> x + y));
        // TODO
        // assertEquals("FA", seq("A").tail().fold("F", (x, y) -> x + y));
        assertEquals("F", seq("").tail().fold("F", (x, y) -> x + y));
    }

    @Test
    public void testHead() {
        assertEquals(Optional.of("java"), seq("java", "scala", "perl", "ruby", "python").head());
        assertEquals(Optional.of("scala"), seq("scala", "perl", "ruby", "python").head());
        assertEquals(Optional.empty(), seq().head());
    }

    @Test
    public void testIndexOf() {
        assertEquals(2, seq("java", "scala", "perl", "ruby", "python").indexOf("perl"));
        assertEquals(-1, seq("java", "scala", "perl", "ruby", "python").indexOf("haskell"));
        assertEquals(-1, seq().indexOf("java"));
    }

    @Test
    public void testIndexWhere() {
        assertEquals(4, seq("java", "scala", "perl", "ruby", "python").indexWhere(x -> x.length() == 6));
        assertEquals(-1, seq("java", "scala", "perl", "ruby", "python").indexWhere(x -> x.length() == 7));
        assertEquals(-1, seq("").tail().indexWhere(x -> x.isEmpty()));
    }

    @Test
    public void testMap() {
        assertEquals(seq("JAVA", "SCALA", "PERL"), seq0(arr("java", "scala", "perl")).map(String::toUpperCase));
    }

    @Test
    public void testMapToDouble() {
        assertEquals(DoubleSequence.of(100.1, 2.3, 33.5),
            seq0(arr("100.1", "2.3", "33.5")).mapToDouble(Double::parseDouble));
    }

    @Test
    public void testMapToInt() {
        assertEquals(IntSequence.of(100, 2, 33), seq0(arr("100", "2", "33")).mapToInt(Integer::parseInt));
    }

    @Test
    public void testMapToLong() {
        assertEquals(LongSequence.of(100, 2, 33), seq0(arr("100", "2", "33")).mapToLong(Long::parseLong));
    }

    @Test
    public void testOfCollectionOfE() {
        assertEquals(seq("A", "B"), Sequence.of(Arrays.asList("A", "B")));
    }

    @Test
    public void testOfEArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfStreamOfE() {
        assertEquals(seq("A", "B"), Sequence.of(Stream.of("A", "B")));
    }

    @Test
    public void testReduce() {
        assertEquals("ACE", seq("A", "C", "E").reduce((x, y) -> x + y).get());
        assertEquals("FACE", seq("A", "C", "E").reduce("F", (x, y) -> x + y));
        assertEquals("c(b(a))", seq("a", "b", "c").reduce((x, y) -> String.format("%s(%s)", y, x)).get());
        assertEquals("c(b(a(x)))", seq("a", "b", "c").reduce("x", (x, y) -> String.format("%s(%s)", y, x)));
        assertEquals(Optional.<String>empty(), seq("").tail().reduce((x, y) -> x + y));
        assertEquals("X", seq("").tail().reduce("X", (x, y) -> x + y));
    }

    @Test
    public void testReverse() {
        String[] arg = arr("java", "scala", "perl", "ruby", "python");
        List<String> a = new ArrayList<>();
        Collections.addAll(a, arg);
        Collections.reverse(a);
        assertEquals(seq(a), seq(arg).reverse());
    }

    @Test
    public void testSeqCollectionOfE() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqEArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSeqStreamOfE() {
        String[] a = arr("java", "scala", "perl", "java", "python");
        assertEquals(seq(a), seq(Stream.of(a)));
    }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSort() {
        String[] a = arr("java", "scala", "perl", "ruby", "python");
        String[] expected = Arrays.copyOf(a, a.length);
        Arrays.sort(expected);
        assertEquals(seq(expected), seq0(a).sort());
    }

    @Test
    public void testSortWith() {
        // fail("Not yet implemented");
    }

    @Test
    public void testStream() {
        assertEquals("@@a1@@bb@@ccc", seq("a1", "bb", "ccc").stream().reduce("", (x, y) -> x + "@@" + y));
    }

    @Test
    public void testSubSequence() {
        assertEquals(seq("scala", "perl", "ruby"),
            seq0(arr("java", "scala", "perl", "ruby", "python")).subSequence(1, 3));
    }

    @Test
    public void testTail() {
        assertEquals(seq("scala", "perl", "ruby"), seq("java", "scala", "perl", "ruby").tail());
        assertEquals(seq("perl", "ruby"), seq("java", "scala", "perl", "ruby").tail().tail());
        assertEquals(seq(), seq("scala").tail().tail());
    }

    @Test
    public void testTake() {
        assertEquals(seq("java", "scala", "perl"), seq("java", "scala", "perl", "ruby").take(3));
        assertEquals(seq(), seq("scala").take(0));
    }

    @Test
    public void testTakeWhile() {
        Sequence<String> i1 = seq("java", "scala", "perl", "ruby");
        assertEquals(seq("java", "scala"), i1.takeWhile(x -> !x.equals("perl")));
        assertEquals(seq("java"), i1.takeWhile(x -> !x.equals("scala")));
        assertEquals(seq(), i1.takeWhile(x -> x.equals("perl")));
    }

    @Test
    public void testToArrayIntFunctionOfE() {
        assertArrayEquals(arr("11", "22"), seq0("11", "22").toArray(String[]::new));
    }

    @Test
    public void testToList() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToMapWithKey() {
        assertEquals("{python=6, java=4, scala=5, perl=4, ruby=4}", //
            seq("java", "scala", "perl", "ruby", "python").toMapWithKey(String::length).toString());
        assertEquals("{1=1, 2=2, 3=3}", seq0("1", "2", "3").toMapWithKey(String::toUpperCase).toString());
    }

    @Test
    public void testToMapWithValue() {
        assertEquals("{JAVA=java, SCALA=scala, PERL=perl, RUBY=ruby, PYTHON=python}",
            seq("java", "scala", "perl", "ruby", "python").toMapWithValue(String::toUpperCase).toString());
        assertEquals("{1=1, 2=2, 3=3}", seq0("1", "2", "3").toMapWithValue(String::toUpperCase).toString());
    }

    @Test
    public void testToSet() {
        assertEquals(new HashSet<>(Arrays.asList("11", "22")), seq0("11", "22").toSet());
    }

    @Test
    public void testToStringArray() {
        assertArrayEquals(arr("11", "22"), seq0("11", "22").toStringArray());
    }

}
