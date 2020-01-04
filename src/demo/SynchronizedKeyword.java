package demo;


public class SynchronizedKeyword {

    private int count = 0;
    private int synchronizedCount = 0;

    private synchronized void increment() {
        synchronizedCount++;
    }

    public static void main(String[] args) {
        SynchronizedKeyword synchronizedKeyword = new SynchronizedKeyword();
        synchronizedKeyword.doWork();
    }

    private void doWork() {
        Thread t1 = new Thread(() ->{
            for (int i = 0; i < 10000; i++) {
                count++;
                increment();
            }
        });
        Thread t2 = new Thread(() ->{
            for (int i = 0; i < 10000; i++) {
                /*
                count++ is equivalent to three operations as under:
                    int temp = count;
                    temp = temp + 1;
                    count = temp;
                 */
                count++;
                // This method is synchronized and two threads can never enter execute it simultaneously
                increment();
            }
        });
        t1.start();
        t2.start();

        try {
            // thread.join() is used to wait for completion of threads.
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /** What should be the output?
         * At first glance it should be 20000 as both the threads are incrementing counter by 10000.
         * But, it does not seems so. It is usually a value lower than 20000.
         * Why? Because incrementing is not an atomic operation and it includes three operations:
         * 1. Read value from memory location.
         * 2. Increase it by one.
         * 3. Write it to memory location.
         *
         * It is possible that both the threads may read some value and
         * increment it by 1 and write and hence some increments are lost.
         * This problems occur when two threads interleave and write a shared resource.
         *
         * Solutions:
         * Volatile Keyword : No because two thread may still read the same value.
         * Synchronized Keyword : Preventing multiple threads to access same code. [However, it degrades performance]
         */
        System.out.println("Count is: " + count);
        System.out.println("Synchronized count is: " + synchronizedCount);
    }
}
