package net.argius.java8.seq;

import static net.argius.java8.seq.Sequence.seq;
import static net.argius.java8.seq.TestUtils.arr;
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public final class SequenceImplTest {

    @SafeVarargs
    static <E> SequenceImpl<E> seq0(E... a) {
        SequenceImpl<E> x = (SequenceImpl<E>)SequenceFactory.createWithCopy(a);
        return x;
    }

    @Test
    public void testAt() {
        // fail("Not yet implemented");
    }

    @Test
    public void testEqualsObject() {
        Sequence<String> seq1 = seq("11", "22");
        Sequence<String> seq2 = seq("11", "22");
        assertEquals(seq1, seq1);
        assertEquals(seq1, seq2);
        assertEquals(seq1, seq("11", "22"));
        assertNotEquals(seq1, null);
        assertNotEquals(seq1, "");
        assertNotEquals(seq1, seq0("11", "22", "33"));
        assertNotEquals(seq1, seq0("11", "33"));
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
    public void testIterator() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = seq0("11", "22").iterator(); it.hasNext();)
            sb.append("AA").append(it.next()).append("ZZ/");
        assertEquals("AA11ZZ/AA22ZZ/", sb.toString());
    }

    @Test
    public void testMap() {
        assertEquals(seq("==11::", "==22::"), seq0("11", "22").map(x -> "==" + x + "::"));
    }

    @Test
    public void testMapToInt() {
        // fail("Not yet implemented");
    }

    @Test
    public void testMapToIntAsArray() {
        // fail("Not yet implemented");
    }

    // @Test
    // public void testFilter() {
    // Sequence<String> seq = seq("java", "scala", "perl", "ruby", "python");
    // assertEquals(seq("java", "perl", "ruby"), seq.filter(x -> x.length() ==
    // 4));
    // }
    //
    // @Test
    // public void testHead() {
    // assertEquals(Optional.of("java"), seq("java", "scala", "perl", "ruby",
    // "python").head());
    // assertEquals(Optional.of("scala"), seq("scala", "perl", "ruby",
    // "python").head());
    // assertEquals(Optional.empty(), seq().head());
    // }
    //
    // @Test
    // public void testTail() {
    // assertEquals(seq("scala", "perl", "ruby"), seq("java", "scala", "perl",
    // "ruby").tail());
    // assertEquals(seq("perl", "ruby"), seq("java", "scala", "perl",
    // "ruby").tail().tail());
    // assertEquals(seq(), seq("scala").tail().tail());
    // }

    @Test
    public void testMapToStringAsArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfCollectionOfE() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfEArray() {
        // fail("Not yet implemented");
    }

    @Test
    public void testOfStreamOfE() {
        // fail("Not yet implemented");
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
        // fail("Not yet implemented");
    }

    // @Test
    // public void testSort() {
    // String[] a = arr("java", "scala", "perl", "ruby", "python");
    // String[] expected = Arrays.copyOf(a, a.length);
    // Arrays.sort(expected);
    // assertEquals(seq(expected), seq(a).sort());
    // }

    @Test
    public void testSize() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSortWith() {
        String[] a = arr("java", "scala", "haskell", "lua", "python");
        String[] expected = Arrays.copyOf(a, a.length);
        Comparator<String> cmp = (x, y) -> IntComparator.NATURAL.compareTo(x.length(), y.length());
        Arrays.sort(expected, cmp);
        assertEquals(seq(expected), seq(a).sortWith(cmp));
    }

    @Test
    public void testStream() {
        // fail("Not yet implemented");
    }

    @Test
    public void testSubSequence() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToArray() {
        assertArrayEquals(arr("11", "22"), seq0("11", "22").toArray());
    }

    @Test
    public void testToArrayIntFunctionOfE() {
        // fail("Not yet implemented");
    }

    @Test
    public void testToList() {
        // fail("Not yet implemented");
    }

    // @Test
    // public void testToMapWithKey() {
    // assertEquals("{python=6, java=4, scala=5, perl=4, ruby=4}", // -
    // seq("java", "scala", "perl", "ruby",
    // "python").toMapWithKey(String::length).toString());
    // assertEquals("{1=1, 2=2, 3=3}", seq("1", "2",
    // "3").toMapWithKey(String::toUpperCase).toString());
    // }
    //
    // @Test
    // public void testToMapWithValue() {
    // assertEquals("{JAVA=java, SCALA=scala, PERL=perl, RUBY=ruby, PYTHON=python}",
    // seq("java", "scala", "perl", "ruby",
    // "python").toMapWithValue(String::toUpperCase).toString());
    // assertEquals("{1=1, 2=2, 3=3}", seq("1", "2",
    // "3").toMapWithValue(String::toUpperCase).toString());
    // }

    @Test
    public void testToSet() {
        assertEquals(new HashSet<>(Arrays.asList("11", "22")), seq0("11", "22").toSet());
    }

    @Test
    public void testToString() {
        assertEquals("[11, 22]", seq(11, 22).toString());
        assertEquals("[11]", seq(11).toString());
        assertEquals("[]", seq().toString());
        assertEquals("[[1, 2], [A, B]]", seq(Arrays.asList("1", "2"), Arrays.asList("A", "B")).toString());
    }

    @Test
    public void testToStringArray() {
        assertArrayEquals(arr("11", "22"), seq0("11", "22").toStringArray());
    }

}
