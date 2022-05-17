import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public class test {
    public static void main(String[] args) throws IOException {
        String[] strings = new String[3];
        strings[0]="line0";
        strings[1]="line1";
        strings[2]="line2";
        Methods.writeFile("UDP_socket.txt",strings);
    }

}
