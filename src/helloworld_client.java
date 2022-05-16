import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.net.*;

public class helloworld_client {
    public static void main(String[] args) throws Exception {
        int client_portNo = 5550, server_portNo = 5555;
        System.out.println("Please input the IP address of destination :");
        BufferedReader uip = new BufferedReader(new InputStreamReader(System.in));
        String serverIP = uip.readLine();
        InetAddress addr = InetAddress.getByName(serverIP);
        while (true) {
            System.out.println("send msg:");
            String msg = uip.readLine();
            byte buffer[] = new byte[msg.length()];
            buffer = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, msg.length(), addr, server_portNo);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
        }
    }
}
