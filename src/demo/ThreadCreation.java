package demo;

class Runner extends Thread {
    @Override
    public void run() {
        ThreadCreation.run("Extending Thread");
    }
}

class Runner1 implements Runnable {
    @Override
    public void run() {
        ThreadCreation.run("Implementing Runnable");
    }
}

/**
 * Demo to show three ways how a thread can be run in java
 * 1. Extending Thread class and starting it.
 * 2. Implementing Runnable interface and passing it to constructor of a thread.
 * 3. Anonymous Runnable to thread's constructor
 */
public class ThreadCreation {

    public static void run(String implementation) {
        for (int i = 0; i < 10; i++) {
            System.out.println(implementation + ": " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Extending thread class
        // This approach is usually avoided because the class would not be able to extend anything.
        Runner runner = new Runner();
        /** We call start() and not run() because start() creates new
         * thread and runs run() method on new thread. If we directly call run(), it won't create new thread.
         */
        runner.start();

        // Implementing Runnable interface - Class can still be extended. Still rarely used.
        Thread thread = new Thread(new Runner1());
        thread.start();

        // Anonymous Runnable - Usually we avoid thread creations like this instead we use executors.
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadCreation.run("Anonmymous Runnable");
            }
        });
        thread1.start();

        // Anonymous Shorthand Since Java 8
        Thread thread2 = new Thread(() -> ThreadCreation.run("Anonymous Runnable Shorthand Since Java 8"));
        thread2.start();

        /** Executors will be discussed later **/
    }
}
