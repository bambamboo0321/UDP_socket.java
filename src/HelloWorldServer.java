import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class HelloWorldServer {

    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[1024];
        while(true) {
            DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
            DatagramSocket socket = new DatagramSocket(5555);
            socket.receive(packet);
            System.out.println(new String(packet.getData(),0,packet.getLength()));
            socket.close();
        }
    }
}