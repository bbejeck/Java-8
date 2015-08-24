package bbejeck.collector;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Test for creating immutable collections from Streams
 */

@SuppressWarnings("unchecked")
public class ImmutableCollectorsTest {



    @Test
    public void testCollectImmutableList(){
        List<String> things = Lists.newArrayList("Apple", "Ajax", "Anna", "banana", "cat", "foo", "dog", "cat");


        List<String> aWords = things.stream().filter(w -> w.startsWith("A")).collect(ImmutableCollectors.ofList());
        assertThat(aWords.contains("Apple"),is(true));
        assertThat(aWords instanceof ImmutableList,is(true));

        assertThat(aWords.size(),is(3));
        boolean unableToModifyList = false;

        try{
            aWords.add("Bad Move");
        }catch (UnsupportedOperationException e){
            unableToModifyList = true;
        }

        assertTrue("Should not be able to modify list",unableToModifyList);
    }

    @Test
    public void testCollectImmutableSet(){

        Set<String> things = Sets.newHashSet("Apple", "Apple", "Apple", "banana", "banana", "banana", "cat", "cat","cat");
        Set<String> uniqueWords = things.stream().collect(ImmutableCollectors.ofSet());

        assertThat(uniqueWords.contains("Apple"),is(true));
        assertThat(uniqueWords.contains("banana"),is(true));
        assertThat(uniqueWords.contains("cat"),is(true));
        assertThat(uniqueWords.size(),is(3));
        assertThat(uniqueWords instanceof ImmutableSet, is(true));

        boolean unableToModifySet = false;

        try{
            uniqueWords.add("Bad Move");
        }catch (UnsupportedOperationException e){
            unableToModifySet = true;
        }

        assertTrue("Should not be able to modify set",unableToModifySet);
    }


}