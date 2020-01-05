package demo4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/** Read List, ArrayList and Vector Documentation */
class Worker {

    private Random random = new Random();
    private List<Integer> list1 = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();
    /**
     * Why don't lock on lists itself?
     * Because the object may itself change. Lock objects must be final.
     * Suppose if it is a number and not list and you took a lock on 4 and then incremented it
     * that may lead to very hard to debug issues. Simple strategy, we take a lock object which is final and
     * is immutable guaranteeing that locking works perfectly.
     */
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    private Vector<Integer> vector1 = new Vector<>();
    private Vector<Integer> vector2 = new Vector<>();

    // This function can be assumed to be the one taking some time to execute
    // like a network call.
    private void stageOne() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list1.add(random.nextInt(100));
    }

    private void stageTwo() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list2.add(random.nextInt(100));
    }

    private void process() {
        for (int i = 0; i < 1000; i++) {
            stageOne();
            stageTwo();
        }
    }

    synchronized private void synchronizedStageOne() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list1.add(random.nextInt(100));
    }

    synchronized private void synchronizedStageTwo() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list2.add(random.nextInt(100));
    }

    private void synchronizedProcess() {
        for (int i = 0; i < 1000; i++) {
            synchronizedStageOne();
            synchronizedStageTwo();
        }
    }

    private void lockStageOne() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock1) {
            list1.add(random.nextInt(100));
        }
    }

    private void lockStageTwo() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (lock2) {
            list2.add(random.nextInt(100));
        }
    }

    private void lockProcess() {
        for (int i = 0; i < 1000; i++) {
            lockStageOne();
            lockStageTwo();
        }
    }

    private void vectorStageOne() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vector1.add(random.nextInt(100));
    }

    private void vectoStageTwo() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vector2.add(random.nextInt(100));
    }

    private void vectorProcess() {
        for (int i = 0; i < 1000; i++) {
            vectorStageOne();
            vectoStageTwo();
        }
    }

    void main() {
        System.out.println("Starting...");
        long start = System.currentTimeMillis();
        process();
        long end = System.currentTimeMillis();
        // This should be a bit greater than 2 * 1000 milliseconds
        // as there is 1ms sleep + time for other operations (Close to 2500ms)
        System.out.println("Time Taken: " + (end - start));
        // Both of them must have 1000 elements
        System.out.println("List1: " + list1.size() + "; List2: " + list2.size());

        list1.clear();
        list2.clear();

        // To ensure list is cleared before some other array accesses it
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Starting...");
        start = System.currentTimeMillis();
        // call process in a new thread
        new Thread(this::process).start();
        end = System.currentTimeMillis();
        // Since work is offloaded to a thread this will take just processing time to start thread (Close to 50ms)
        System.out.println("Time Taken: " + (end - start));
        // This should be 0 because process() has been called in new thread which would have significant
        // overhead of thread creation. So, this will run first.
        System.out.println("List1: " + list1.size() + "; List2: " + list2.size());

        list1.clear();
        list2.clear();

        // To ensure list is cleared before some other array accesses it
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Starting...");
        start = System.currentTimeMillis();
        Thread t1 = new Thread(this::process);
        Thread t2 = new Thread(this::process);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        end = System.currentTimeMillis();
        // As we are waiting for both threads to finish this should be ideally a bit more than 2000ms (Close to 2500ms)
        System.out.println("Time Taken: " + (end - start));
        // We expect this to be 2000 for both lists but it is not so since two threads are concurrently accessing
        // the list and ArrayList in itself is not a thread safe collection
        // Real output comes greater than 2000.
        System.out.println("List1: " + list1.size() + "; List2: " + list2.size());

        // A simple solution would be to synchronize methods using synchronized keyword.
        // But since synchronized will use lock on current object only the performance will worsen.
        list1.clear();
        list2.clear();

        // To ensure list is cleared before some other array accesses it
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Starting...");
        start = System.currentTimeMillis();
        Thread t3 = new Thread(this::synchronizedProcess);
        Thread t4 = new Thread(this::synchronizedProcess);
        t3.start();
        t4.start();

        try {
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        end = System.currentTimeMillis();
        // performance worsens because different shared data is using same lock [More than 5000ms]
        System.out.println("Time Taken: " + (end - start));
        // This works fine now; 2000 elements per list
        System.out.println("List1: " + list1.size() + "; List2: " + list2.size());

        /** Two locks solution */
        list1.clear();
        list2.clear();

        // To ensure list is cleared before some other array accesses it
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Starting...");
        start = System.currentTimeMillis();
        Thread t5 = new Thread(this::lockProcess);
        Thread t6 = new Thread(this::lockProcess);
        t5.start();
        t6.start();

        try {
            t5.join();
            t6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        end = System.currentTimeMillis();
        // performance should improve here (Close t0 2600ms)
        System.out.println("Time Taken: " + (end - start));
        // This works fine now; 2000 elements per list
        System.out.println("List1: " + list1.size() + "; List2: " + list2.size());

        /**
         * Another solution would be to use thread safe collections like Vector
         * Please see that Vector is equivalent to ArrayList except its methods are synchronized.
         */
        System.out.println("Starting...");
        start = System.currentTimeMillis();
        Thread t7 = new Thread(this::vectorProcess);
        Thread t8 = new Thread(this::vectorProcess);
        t7.start();
        t8.start();

        try {
            t7.join();
            t8.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        end = System.currentTimeMillis();
        // Same performance as above since this is a thread safe structure.
        System.out.println("Time Taken: " + (end - start));
        // This works fine now; 2000 elements per list
        System.out.println("Vector1: " + vector1.size() + "; Vector2: " + vector2.size());
    }
}
