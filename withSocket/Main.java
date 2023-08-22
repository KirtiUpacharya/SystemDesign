
import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final Socket socket;

    public Producer(BlockingQueue<Integer> queue, Socket socket) {
        this.queue = queue;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ) {
            for (int i = 1; i <= 10; i++) {
                System.out.println("Producing: " + i);
                queue.put(i);
                out.writeObject(i);
                Thread.sleep(100);
            }
            out.writeObject(-1); // Signal the end of production
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final Socket socket;

    public Consumer(BlockingQueue<Integer> queue, Socket socket) {
        this.queue = queue;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            while (true) {
                int value = queue.take();
                System.out.println("Consuming: " + value);
                int receivedValue = (int) in.readObject();
                if (receivedValue == -1) {
                    break; // End of production
                }
                Thread.sleep(200);
            }
        } catch (IOException | InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        try (
            ServerSocket serverSocket = new ServerSocket(12345);
            Socket producerSocket = serverSocket.accept();
            Socket consumerSocket = serverSocket.accept();
        ) {
            Thread producerThread = new Thread(new Producer(queue, producerSocket));
            Thread consumerThread = new Thread(new Consumer(queue, consumerSocket));

            producerThread.start();
            consumerThread.start();

            producerThread.join();
            consumerThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}







/*In the code you provided, the final keyword is used in a few places to indicate that a variable or reference cannot be changed once it has been assigned a value. The use of final has several implications in Java:

Immutable Reference: When a reference is declared as final, it means that the reference itself cannot be reassigned to point to a different object. However, the object that the reference points to can still be modified (if it's mutable).

Thread Safety: Declaring a variable as final can contribute to thread safety in some cases. Once a final variable is assigned a value, it becomes a constant, and other threads accessing it are guaranteed to see the same value.

Compiler Optimization: The final keyword can allow the compiler to perform certain optimizations, knowing that the value of the variable won't change. This can potentially improve performance.

Here's where final is used in your code and why:

java
Copy code
private final BlockingQueue<Integer> queue;
In this case, the queue reference in the Producer and Consumer classes is declared as final. This means that once a BlockingQueue instance is assigned to this reference, it cannot be changed to point to a different BlockingQueue instance. This can be helpful in multithreaded scenarios to ensure that the same queue instance is consistently used by both the producer and consumer.

java
Copy code
private final Socket socket;
The socket reference in the Producer and Consumer classes is also declared as final. This ensures that once a Socket instance is assigned to this reference, it cannot be changed to point to a different Socket instance. In a multithreaded context, using a final reference can help avoid accidental reassignment or interference between threads accessing the same variable.

java
Copy code
final int receivedValue = (int) in.readObject();
In this case, receivedValue is declared as final inside the Consumer class. This is a local variable that is assigned a value from the socket's input stream. By declaring it as final, you're ensuring that its value won't change after assignment. This can be useful for clarity and avoiding accidental changes to the value later in the code.

In summary, the use of the final keyword in your code helps enforce immutability and can contribute to safer and more predictable multithreaded behavior, as well as aiding in code readability and performance optimization.

*/



