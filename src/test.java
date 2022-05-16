import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public class test {
    public static void main(String[] args) {
        byte []buffer = new byte[1024];
        String ip = "192.168.0.1";
        System.out.println(ip.getBytes(StandardCharsets.UTF_8));

    }

}
