import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class server_socket {
    private final byte[] buffer = new byte[1000];
    private final int portNo;
    private String clientIp;

    public server_socket(int portNo) {
        this.portNo = portNo;
        System.out.println("Server端開始接受連線請求!");   //print出的文字訊息
    }

    public String receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(this.buffer, this.buffer.length);
        DatagramSocket socket = new DatagramSocket(this.portNo);
        socket.receive(packet);   //接收封包
        this.clientIp = packet.getAddress().toString().replaceAll("/", "");
        String msg = new String(this.buffer, 0, packet.getLength());   //將訊息轉為字串
        socket.close();    //關閉socket
        System.out.println("Client " + clientIp + ":" + msg);
        return msg;
    }


    public void sendMessage(String msg) throws IOException {
        int oLength = msg.length();     //將訊息長度大小放到oLength
        byte[] sendBuffer = new byte[oLength];
        sendBuffer = msg.getBytes();
        DatagramPacket packet =
                new DatagramPacket(sendBuffer, oLength, InetAddress.getByName(this.clientIp), portNo);    //訊息封包(值,大小,位址,port)
        DatagramSocket socket = new DatagramSocket();  //建立socket
        socket.send(packet);    //送出packet封包訊息
        socket.close();         //關閉socket
    }
}
