package org.crawler.crawl;

import org.crawler.config.Configuration;
import org.crawler.console.Console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static org.crawler.console.Color.Red;
import static org.crawler.console.Color.Yellow;

public class ConcurrencyHandler {
    private Configuration config;
    private ExecutorService executorService;
    private Collection<Callable<Page>> callables = new ArrayList<>();

    private List<Future<Page>> threads = new ArrayList<>();

    public ConcurrencyHandler (Configuration config) {
        this.config = config;

        init();
        createThreads();
    }

    /**
     * This method starts all threads and waits for them to finish.
     * Note, that this method must not be called. It is called internally by getResults() if it has not been called.
     */
    public void start () {
        if (!this.threads.isEmpty()) return;

        Console.print(Yellow, "Starting threads...");
        try {
            this.threads = executorService.invokeAll(callables);
        } catch (InterruptedException e) {
            Console.print(Red, "Invoking Threads failed");
        }
    }

    /**
     * This method waits internally for all threads to finish and returns the results.
     * @return A list of PageInfo's that holds the results of each Crawler
     */
    public List<Page> getResults () {
        if (threads.isEmpty()) start();

        List<Page> results = new ArrayList<>();

        for (Future<Page> future : threads) {
            try {
                results.add(future.get());
            } catch (ExecutionException | InterruptedException e) {
                Console.print(Red, "Getting Thread-Results did throw an Exception");
            }
        }


        executorService.shutdown();
        return results;
    }

    /**
     * This method initializes the ConcurrencyHandler by clearing the callables and creating a new ExecutorService.
     */
    private void init () {
        callables.clear();
        executorService = Executors.newFixedThreadPool(config.getUrls().length);
    }

    /**
     * This method creates a new Callable for each URL in the Configuration and adds it to the callables list.
     */
    private void createThreads () {
        for (int i = 0; i < config.getUrls().length; i++) {
            int finalI = i;
            Callable<Page> callable = () -> {
                Crawler c1 = new Crawler(config);
                return c1.crawl(config.getUrls()[finalI]);
            };
            callables.add(callable);
        }
    }

}
