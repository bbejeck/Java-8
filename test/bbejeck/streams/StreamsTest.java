package bbejeck.streams;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * User: Bill Bejeck
 * Date: 4/22/14
 * Time: 10:35 PM
 */
public class StreamsTest {

    private UnaryOperator<String> lowercase = String::toLowerCase;
    private UnaryOperator<String> replaceVowel = s -> s.replaceAll("[aeiou]", "*");
    private UnaryOperator<String> trim = String::trim;
    private Function<String, String> formatString = lowercase.compose(trim).andThen(replaceVowel);


    private Predicate<Integer> lessThan100 = n -> n < 100;
    private Predicate<Integer> greaterThan25 = n -> n > 25;
    private Predicate<Integer> multipleOf3 = n -> n % 3 == 0;
    private Predicate<Integer> composedPredicate = lessThan100.and(greaterThan25).or(multipleOf3);

    @Test
    public void test_transform_list() {

        List<String> words = Arrays.asList("FOO", "BOoK", "BAR", "BEACH", "StUFf");
        List<String> expectedWords = Arrays.asList("f**", "b**k", "b*r", "b**ch", "st*ff");
        List<String> transformed = words.stream().map(formatString).collect(Collectors.toList());
        assertThat(transformed, is(expectedWords));
    }

    @Test
    public void test_filter_list() {
        List<Integer> numbers = Arrays.asList(77, 24, 23, 98, 150, 1000, 19, 107, 8);
        List<Integer> filtered = numbers.stream().filter(composedPredicate).collect(Collectors.toList());
        List<Integer> expected = Arrays.asList(77, 24, 98, 150);
        assertThat(filtered, is(expected));
    }

    @Test
    public void test_combine_streams() {
        Stream<String> s = Stream.of("FOO");
        Stream<String> s2 = Stream.of("Moe", "Larry", "Curly");
        List<String> combined = Stream.concat(s, s2).collect(Collectors.toList());
        List<String> expected = Arrays.asList("FOO", "Moe", "Larry", "Curly");
        assertThat(combined, is(expected));
    }

    @Test
    public void test_summing_int_stream() {
        Stream<Integer> numbers = Stream.of(1, 2, 3, 10, 15, 100, 250);
        int total = numbers.collect(Collectors.summingInt(i -> i));
        assertThat(total, is(381));
    }

    @Test
    public void test_generate_stream() {
        Stream<String> generated = Stream.generate(() -> RandomStringUtils.randomAlphabetic(25)).limit(10);
        assertThat(generated.count(), is(10L));
    }

    @Test(expected = AssertionError.class)
    public void test_change_stream_source() {
        List<String> words = Lists.newArrayList("FOO", "BOoK", "BAR", "BEACH", "StUFf");
        List<String> expected = Arrays.asList("foo", "book", "bar", "beach", "stuff");
        Stream<String> wordStream = words.stream().map(String::toLowerCase);
        words.remove(0);
        words.remove(0);
        List<String> lowerCaseStrings = wordStream.collect(Collectors.toList());
        assertThat(lowerCaseStrings, is(expected));
        fail("List from modified collection should not match expected");
    }

    @Test
    public void test_use_defensive_copy_for_stream() {
        List<String> words = Lists.newArrayList("FOO", "BOoK", "BAR", "BEACH", "StUFf");
        List<String> defensiveCopy = new ArrayList<>(words);
        List<String> expected = Arrays.asList("foo", "book", "bar", "beach", "stuff");
        Stream<String> wordStream = defensiveCopy.stream().map(String::toLowerCase);
        words.remove(0);
        words.remove(0);
        List<String> lowerCaseStrings = wordStream.collect(Collectors.toList());
        assertThat(lowerCaseStrings, is(expected));
    }

    @Test
    public void test_using_unmodifiable_collections() {
        List<String> words = ImmutableList.of("FOO", "BOoK", "BAR", "BEACH", "StUFf");
        List<String> expected = Arrays.asList("foo", "book", "bar", "beach", "stuff");

        Stream<String> wordStream = words.stream().map(String::toLowerCase);
        //subList is also immutable
        //Effectively same as previous test, removing the first two elements
        List<String> subList = words.subList(2,words.size());

        List<String> lowerCaseStrings = wordStream.collect(Collectors.toList());
        assertThat(lowerCaseStrings, is(expected));
    }

    @Test
    public void test_parallel_streams_sort() {
        List<Integer> numbers = Lists.newArrayList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        List<Integer> expectedOrder = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> sorted = numbers.parallelStream().sorted().collect(Collectors.toList());
        assertThat(sorted, is(expectedOrder));
    }

    @Test
    public void test_stream_filter_with_partial_function() {
        BiFunction<Integer, Integer, Boolean> modN = (n, x) -> (x % n) == 0;
        Predicate<Integer> modFive = i -> modN.apply(5, i);
        Stream<Integer> numbers = Stream.of(3, 5, 14, 25, 24, 75);
        List<Integer> filtered = numbers.filter(modFive).collect(Collectors.toList());
        assertThat(filtered, is(Arrays.asList(5, 25, 75)));
    }

    @Test
    public void test_reduce() {
        Stream<Integer> numbers = Stream.of(2,3,4);
        BiFunction<Integer, Integer, Integer> mult = (i, j) -> i * j;
        BinaryOperator<Integer> combiner = (n,m) -> {
            System.out.println("combiner n "+n+" m "+m);
            return -1;
        };
        Integer sum = numbers.reduce(1, mult,combiner);
        System.out.println(sum);
    }

    @Test
    public void test_reduce_string_concat() {
        Stream<String> values = Stream.of("7","8","9");
        Optional<String> combined = values.reduce((x,y)-> x+":"+y);
        assertThat(combined.get(),is("7:8:9"));
    }


}
