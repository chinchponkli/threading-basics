package demo;

import java.util.Scanner;

class Processor extends Thread {

    /**
     * volatile prevents threads from caching variables locally on CPU
     * If this is not volatile the thread running run() method will use locally cached value of running
     * which may cause the program to continue to run() even if we are calling shutDown() from another thread
     * as in main method
     * */
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            // Thread.toString() prints current threads name, priority and group
            System.out.println("Current thread: " + Thread.currentThread().toString());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutDown() {
        System.out.println("Current thread: " + Thread.currentThread().toString());
        running = false;
    }
}

public class BasicSynchronizationProblem {

    public static void main(String[] args) {
        Processor processor1 = new Processor();
        processor1.start();

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        // this runs on main thread
        processor1.shutDown();
    }

    /**
     * Key lesson here - if you are modifying variables used by a thread from another thread make it volatile
     * to avoid thread using local copy of the same. Or, you can use some sort of synchronization mechanism.
     *
     * The Singleton instance is also made volatile for the same reason. Otherwise, a thread may use local
     * value which is null and may create another instance.
     */
}
