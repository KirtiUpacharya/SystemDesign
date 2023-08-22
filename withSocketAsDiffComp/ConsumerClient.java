import java.io.*;
import java.net.*;

public class ConsumerClient {
    public static void main(String[] args) {
        try (
            Socket consumerSocket = new Socket("localhost", 12345);
            ObjectInputStream in = new ObjectInputStream(consumerSocket.getInputStream());
        ) {
            while (true) {
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
