package demo4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Read ExecutorService and Executors Documentation.
 */

/**
 * Problem - create ten threads running processors.
 * We may create ten threads and start them using for loop simple.
 * But it comes at cost because of overhead of thread creation and destruction.
 * So, we use thread pools that has active threads and a manager thread assigns task to them once they are free.
 * We use Java's ExecutorService for this.
 */
class Processor implements Runnable {
    private int id;

    Processor(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Starting: " + id);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Completed: " + id);
    }
}

public class ThreadPoolDemo {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 30000; i++) {
            Thread t1 = new Thread(new Processor(i * 2));
            Thread t2 = new Thread(new Processor(i * 2 + 1));
            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        // This takes around 45 seconds to complete.
        System.out.println("Time taken: " + (end - start));

        // Creates a thread pool of fixed size
        // executor has their own managerial thread
        start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 60000; i++) {
            executor.submit(new Processor(i));
        }
        // this tells executor to not to accept any more tasks
        executor.shutdown();
        /** throws RejectedExecutionException */
        //executor.submit(new Processor(1256));
        System.out.println("All Tasks Submitted");
        try {
            /** awaits for all tasks to complete for duration.
             * If doesn't complete code moves forward.
             */
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        end = System.currentTimeMillis();
        // This takes around 37 seconds to complete.
        System.out.println("Time taken: " + (end - start));
    }
}
