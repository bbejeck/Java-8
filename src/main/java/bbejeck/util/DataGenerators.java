package bbejeck.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * User: Bill Bejeck
 * Date: 4/15/14
 * Time: 11:05 PM
 */
public class DataGenerators {

    public static List<String> stringGenerator(long size){
        return Stream.generate(()-> RandomStringUtils.randomAlphabetic(25)).limit(size).collect(Collectors.toList());
    }
}
