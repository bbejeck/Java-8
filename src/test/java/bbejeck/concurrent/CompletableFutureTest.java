package bbejeck.concurrent;

import bbejeck.function.throwing.ThrowingSupplier;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * User: Bill Bejeck
 * Date: 1/4/16
 * Time: 9:03 PM
 */
public class CompletableFutureTest {

    private static ExecutorService service = Executors.newCachedThreadPool();


    @Test
    public void test_accept_either_async_calling_finishes_first() throws Exception {

        CompletableFuture<String> callingCompletable = CompletableFuture.supplyAsync(simTask(1,"calling"),service);
        CompletableFuture<String> nestedCompleteable = CompletableFuture.supplyAsync(simTask(2,"nested"),service);

        List<String> results = new ArrayList<>();
        CompletableFuture<Void> collector = callingCompletable.acceptEither(nestedCompleteable,results::add);

        pauseSeconds(2);
        assertThat(collector.isDone(),is(true));
        assertThat(results.size(),is(1));
        assertThat(results.contains("calling"),is(true));
    }


    @Test
    public void test_accept_either_async_nested_finishes_first() throws Exception {

        CompletableFuture<String> callingCompletable = CompletableFuture.supplyAsync(simTask(2,"calling"),service);
        CompletableFuture<String> nestedCompleteable = CompletableFuture.supplyAsync(simTask(1,"nested"),service);

        List<String> results = new ArrayList<>();
        CompletableFuture<Void> collector = callingCompletable.acceptEither(nestedCompleteable,results::add);

        pauseSeconds(2);
        assertThat(collector.isDone(),is(true));
        assertThat(results.size(),is(1));
        assertThat(results.contains("nested"),is(true));
    }




     private ThrowingSupplier<String> simTask(int numSeconds, String taskResult){
          return ()->{
              Thread.sleep(numSeconds * 1000);
              return taskResult;
          };
     }


    private void pauseSeconds(int seconds) throws Exception {
        Thread.sleep(seconds * 1000);
    }

}
