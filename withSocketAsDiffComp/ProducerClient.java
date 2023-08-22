import java.io.*;
import java.net.*;

public class ProducerClient {
    public static void main(String[] args) {
        try (
            Socket producerSocket = new Socket("localhost", 12345);
            ObjectOutputStream out = new ObjectOutputStream(producerSocket.getOutputStream());
        ) {
            for (int i = 1; i <= 10; i++) {
                out.writeObject(i);
                Thread.sleep(100);
            }
            out.writeObject(-1); // Signal the end of production
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
