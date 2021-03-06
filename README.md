Seq-for-Java8
========================================

Seq-for-Java8 - the library of sequencial containers which is neither Collection nor Stream

**Deprecated.**

This library replaced with a set of `ImmArray` classes in Minestra
https://github.com/argius/minestra


Description
--------------------

Sequences in Seq-for-Java8 are sequencial containers with few functional methods.

These interfaces have been chosen with reference to Streams in java.util.stream and Seq in Scala.
However, Sequences are immutable and stateless.


Examples
--------------------

```
// import net.argius.java8.seq.Sequence;
Sequence<String> seq = Sequence.of("ant", "bean", "coffee");
List<String> list = seq.filter(x -> !x.equals("ant")).reverse().toList();
// => [coffee, bean]

Map<String, Class<?>> y = Sequence.<Class<?>>of(String.class, Integer.class)
    .toMapWithValue(x -> x.getSimpleName());
// => {Integer=class java.lang.Integer, String=class java.lang.String}

// import static net.argius.java8.seq.IntSequence.seq;
double average = seq(-100, 13, 25, 8, 1).drop(1).average();
// => 11.75
```


Notice
--------------------

These interfaces may be changed without notice during the beta version.


License
--------------------

Apache License 2.0
