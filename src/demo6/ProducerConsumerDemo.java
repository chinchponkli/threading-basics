package demo6;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/** Read documentation for BlockingQueue, ArrayBlockingQueue */
public class ProducerConsumerDemo {

    /** A thread-safe blocking queue (fifo data structure) of size 10 */
    private static BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                producer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                consumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    private static void producer() throws InterruptedException {
        Random random = new Random();
        // fast producer
        while (true) {
            /** This will wait before consumer removes an item from Queue */
            /** Since the operations are synchronized, there won't be any problem */
            blockingQueue.put(random.nextInt(100));
        }
    }

    private static void consumer() throws InterruptedException {
        Random random = new Random();
        // On an average it takes only one value per second
        while (true) {
            Thread.sleep(100);
            if (random.nextInt(10) == 0) {
                /** take() similarly waits until it has something to take from the queue() */
                Integer value = blockingQueue.take();
                System.out.println("Taken value: " + value + "; queue size is: " + blockingQueue.size());
            }
        }
    }
}
