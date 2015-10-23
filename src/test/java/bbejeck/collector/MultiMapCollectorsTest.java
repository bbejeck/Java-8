package bbejeck.collector;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by bbejeck on 10/22/15.
 * Tests for Multimap collectors
 */
@SuppressWarnings("unchecked")
public class MultiMapCollectorsTest {

    private List<TestObject> testObjectList;

    @Before
    public void setUp() {
        TestObject w1 = new TestObject("one", "stuff");
        TestObject w2 = new TestObject("one", "foo");
        TestObject w3 = new TestObject("two", "bar");
        TestObject w4 = new TestObject("two", "bar");
        testObjectList = Arrays.asList(w1, w2, w3, w4);

    }

    @Test
    public void testListMultimap() throws Exception {
        ArrayListMultimap<String, TestObject> testMap = testObjectList.stream().collect(MultiMapCollectors.listMultimap((TestObject w) -> w.id));

        assertThat(testMap.size(), is(4));
        assertThat(testMap.get("one").size(), is(2));
        assertThat(testMap.get("two").size(), is(2));
    }

    @Test
    public void testSetMultimap() throws Exception {
        HashMultimap<String, TestObject> testMap = testObjectList.stream().collect(MultiMapCollectors.setMulitmap((TestObject w) -> w.id));

        assertThat(testMap.size(), is(3));
        assertThat(testMap.get("one").size(), is(2));
        assertThat(testMap.get("two").size(), is(1));
    }


    private static class TestObject {
        public String id;
        String category;


        public TestObject(String id, String category) {
            this.id = id;
            this.category = category;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestObject)) return false;

            TestObject testObject = (TestObject) o;

            if (!id.equals(testObject.id)) return false;
            return category.equals(testObject.category);

        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + category.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Widget{" +
                    "id='" + id + '\'' +
                    ", category='" + category + '\'' +
                    '}';
        }
    }
}