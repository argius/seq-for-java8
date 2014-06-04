package net.argius.java8.seq;

import org.junit.runner.*;
import org.junit.runners.*;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ SequenceTest.class, SequenceImplTest.class, IntSequenceTest.class, IntSequenceImplTest.class,
        IntComparatorTest.class, LongSequenceTest.class, LongSequenceImplTest.class, LongComparatorTest.class,
        DoubleSequenceTest.class, DoubleSequenceImplTest.class, DoubleComparatorTest.class })
public class AllTests {
    //
}
