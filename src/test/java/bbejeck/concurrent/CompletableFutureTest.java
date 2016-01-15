package bbejeck.concurrent;

import bbejeck.function.throwing.ThrowingSupplier;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * User: Bill Bejeck
 * Date: 1/4/16
 * Time: 9:03 PM
 */
public class CompletableFutureTest {

    private static ExecutorService service = Executors.newCachedThreadPool();
    private List<String> results = new ArrayList<>();

    //Examples of creating simple CompletableFuture objects

    @Test
    public void test_completed_future() throws Exception {
        String expectedValue = "the expected value";
        CompletableFuture<String> alreadyCompleted = CompletableFuture.completedFuture(expectedValue);
        assertThat(alreadyCompleted.get(), is(expectedValue));
    }

    @Test
    public void test_run_async() throws Exception {
        CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> System.out.println("running async task"), service);
        pauseSeconds(1);
        assertThat(runAsync.isDone(), is(true));
    }

    @Test
    public void test_supply_async() throws Exception {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(simulatedTask(1, "Final Result"), service);
        assertThat(completableFuture.get(), is("Final Result"));
    }


    //Consuming results of CompletableFutures

    @Test
    public void test_accept_result() throws Exception {
        CompletableFuture<String> task = CompletableFuture.supplyAsync(simulatedTask(1, "add when done"), service);
        CompletableFuture<Void> acceptingTask = task.thenAccept(results::add);
        pauseSeconds(2);
        assertThat(acceptingTask.isDone(), is(true));
        assertThat(results.size(), is(1));
        assertThat(results.contains("add when done"), is(true));

    }

    @Test
    public void test_accept_either_async_calling_finishes_first() throws Exception {

        CompletableFuture<String> callingCompletable = CompletableFuture.supplyAsync(simulatedTask(1, "calling"), service);
        CompletableFuture<String> nestedCompletable = CompletableFuture.supplyAsync(simulatedTask(2, "nested"), service);

        CompletableFuture<Void> collector = callingCompletable.acceptEither(nestedCompletable, results::add);

        pauseSeconds(2);
        assertThat(collector.isDone(), is(true));
        assertThat(results.size(), is(1));
        assertThat(results.contains("calling"), is(true));
    }


    @Test
    public void test_accept_either_async_nested_finishes_first() throws Exception {

        CompletableFuture<String> callingCompletable = CompletableFuture.supplyAsync(simulatedTask(2, "calling"), service);
        CompletableFuture<String> nestedCompletable = CompletableFuture.supplyAsync(simulatedTask(1, "nested"), service);

        CompletableFuture<Void> collector = callingCompletable.acceptEither(nestedCompletable, results::add);

        pauseSeconds(2);
        assertThat(collector.isDone(), is(true));
        assertThat(results.size(), is(1));
        assertThat(results.contains("nested"), is(true));
    }

    @Test
    public void test_accept_both_async() throws Exception {

        CompletableFuture<String> firstTask = CompletableFuture.supplyAsync(simulatedTask(3, "first task"), service);
        CompletableFuture<String> secondTask = CompletableFuture.supplyAsync(simulatedTask(2, "second task"), service);

        BiConsumer<String, String> acceptBothResults = (f, s) -> {
            results.add(f);
            results.add(s);
        };


        CompletableFuture<Void> bothTasks = firstTask.thenAcceptBothAsync(secondTask, acceptBothResults);
        pauseSeconds(4);
        assertThat(bothTasks.isDone(), is(true));
        assertThat(results.size(), is(2));
        assertThat(results.get(0), is("first task"));
        assertThat(results.get(1), is("second task"));

    }

    //Applying functions to CompletableFutures
    @Test
    public void test_apply() throws Exception {
        CompletableFuture<String> task = CompletableFuture.supplyAsync(simulatedTask(1, "change me"), service).thenApply(String::toUpperCase);
        assertThat(task.get(), is("CHANGE ME"));
    }


    @Test
    public void test_then_combine_async() throws Exception {
        CompletableFuture<String> firstTask = CompletableFuture.supplyAsync(simulatedTask(3, "combine all"), service);
        CompletableFuture<String> secondTask = CompletableFuture.supplyAsync(simulatedTask(2, "task results"), service);
        CompletableFuture<String> combined = firstTask.thenCombine(secondTask, (f, s) -> f + " " + s);

        assertThat(combined.get(), is("combine all task results"));
    }

    @Test
    public void test_then_combine_with_one_supplied_value() throws Exception {
        CompletableFuture<String> asyncComputedValue = CompletableFuture.supplyAsync(simulatedTask(2, "calculated value"), service);
        CompletableFuture<String> knowValueToCombine = CompletableFuture.completedFuture("known value");

        BinaryOperator<String> calcResults = (f, s) -> "taking a " + f + " then adding a " + s;
        CompletableFuture<String> combined = asyncComputedValue.thenCombine(knowValueToCombine, calcResults);

        assertThat(combined.get(), is("taking a calculated value then adding a known value"));

    }

    @Test
    public void test_then_compose() throws Exception {
        CompletableFuture<List<Integer>> getMultiples = CompletableFuture.supplyAsync(getFirstTenMultiples(13),service);

        Function<List<Integer>,CompletableFuture<Integer>> sumNumbers = multiples -> CompletableFuture.supplyAsync(() -> multiples.stream().mapToInt(Integer::intValue).sum());

        CompletableFuture<Integer> summedMultiples = getMultiples.thenComposeAsync(sumNumbers,service);

        assertThat(summedMultiples.get(),is(715));
    }


    private ThrowingSupplier<String> simulatedTask(int numSeconds, String taskResult) {
        return () -> {
            Thread.sleep(numSeconds * 1000);
            return taskResult;
        };
    }

    private Supplier<List<Integer>> getFirstTenMultiples(int num) {
        return () -> {
            List<Integer> tenMultiples = new ArrayList<>(10);
            for (int i = 1; i <= 10; i++) {
                tenMultiples.add(num * i);
            }
            return tenMultiples;
        };
    }


    private void pauseSeconds(int seconds) throws Exception {
        Thread.sleep(seconds * 1000);
    }

}
