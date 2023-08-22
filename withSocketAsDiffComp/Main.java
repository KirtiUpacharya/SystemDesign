public class Main {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        try (
            ServerSocket serverSocket = new ServerSocket(12345);
            Socket producerSocket = serverSocket.accept();
            Socket consumerSocket = serverSocket.accept();
        ) {
            Thread producerThread = new Thread(new ProducerClient(queue, producerSocket));
            Thread consumerThread = new Thread(new ConsumerClient(queue, consumerSocket));

            producerThread.start();
            consumerThread.start();

            producerThread.join();
            consumerThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
