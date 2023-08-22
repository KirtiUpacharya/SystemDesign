import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

// The Producer class implements the Runnable interface,
// which allows it to be executed in a separate thread.
class Producer implements Runnable {
    private final BlockingQueue<Integer> queue; // Shared blocking queue to store produced items

    // Constructor to initialize the queue
    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            // The producer produces integers from 1 to 10
            for (int i = 1; i <= 10; i++) {
                System.out.println("Producing: " + i);
                queue.put(i); // Put the item in the queue
                Thread.sleep(100); // Simulate some work
            }
        } catch (InterruptedException e) {
            // Restore the interrupted status and exit
            Thread.currentThread().interrupt();
        }
    }
}

// The Consumer class also implements the Runnable interface.
class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue; // Shared blocking queue to retrieve items

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int value = queue.take(); // Take an item from the queue
                System.out.println("Consuming: " + value);
                Thread.sleep(200); // Simulate some work
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5); // Create a blocking queue of size 5

        // Create producer and consumer threads
        Thread producerThread = new Thread(new Producer(queue));
        Thread consumerThread = new Thread(new Consumer(queue));

        // Start the threads
        producerThread.start();
        consumerThread.start();

        // Let the threads run for a while
        try {
            Thread.sleep(3000); // Allow the threads to run for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interrupt the threads to signal them to stop
        producerThread.interrupt();
        consumerThread.interrupt();
    }
}
