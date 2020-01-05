package demo6;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Read CountdownLatch Documentation */
class Processor implements Runnable {
    private CountDownLatch latch;
    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("Started.");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        latch.countDown();
    }
}

public class CountDownLatchDemo {

    public static void main(String[] args) {
        /** Let's wait one or more threads until count reaches zero */
        CountDownLatch latch = new CountDownLatch(3);

        /** creates three threads */
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 9; i++) {
            executor.submit(new Processor(latch));
        }

        try {
            /** waits until countdown latch is zero */
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /** this is guaranteed to print after three "Started." messages */
        /** But can print before 4th, 5th, 6th ond 7th Started message and this is non-deterministic */
        System.out.println("Completed.");
        executor.shutdown();
    }
}
