package bbejeck.lambdas;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Bill Bejeck
 * Date: 8/8/14
 * Time: 9:30 PM
 */
public class MethodReferenceExamples {

    List<String> strings = Arrays.asList("FOO", "BAR","BAZ");
    List<String> lowerCaseStrings = strings.stream().map(String::toLowerCase).collect(Collectors.toList());
    List<String> lowerCaseStringsII = strings.stream().map(x -> x.toLowerCase()).collect(Collectors.toList());

}
