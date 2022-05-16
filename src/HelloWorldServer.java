import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HelloWorldServer {

    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[1024];
        while(true) {
            DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
            DatagramSocket socket = new DatagramSocket(5555);
            System.out.println(InetAddress.getLocalHost().getHostAddress() + ":" + socket.getLocalPort());
            socket.receive(packet);
            System.out.println(new String(packet.getData(),0,packet.getLength()));
            socket.close();
        }
    }
}